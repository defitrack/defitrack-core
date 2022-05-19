package io.defitrack.protocol.compound.lending

import io.defitrack.abi.ABIResource
import io.defitrack.common.network.Network
import io.defitrack.common.utils.FormatUtilsExtensions.asEth
import io.defitrack.evm.contract.ContractAccessorGateway
import io.defitrack.lending.LendingMarketService
import io.defitrack.lending.domain.BalanceFetcher
import io.defitrack.lending.domain.LendingMarket
import io.defitrack.price.PriceRequest
import io.defitrack.price.PriceResource
import io.defitrack.protocol.Protocol
import io.defitrack.protocol.compound.CompoundComptrollerContract
import io.defitrack.protocol.compound.CompoundService
import io.defitrack.protocol.compound.CompoundTokenContract
import io.defitrack.token.ERC20Resource
import io.defitrack.token.TokenType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

@Component
class CompoundLendingMarketService(
    private val contractAccessorGateway: ContractAccessorGateway,
    private val abiResource: ABIResource,
    private val erC20Resource: ERC20Resource,
    private val compoundService: CompoundService,
    private val priceResource: PriceResource
) : LendingMarketService() {

    val comptrollerABI by lazy {
        abiResource.getABI("compound/comptroller.json")
    }

    val cTokenABI by lazy {
        abiResource.getABI("compound/ctoken.json")
    }

    override suspend fun fetchLendingMarkets(): List<LendingMarket> = coroutineScope {
        getTokenContracts().map {
            async(Dispatchers.IO.limitedParallelism(5)) {
                toLendingMarket(it)
            }
        }.awaitAll().filterNotNull()
    }

    private fun toLendingMarket(it: CompoundTokenContract): LendingMarket? {
        return try {
            it.underlyingAddress?.let { tokenAddress ->
                erC20Resource.getTokenInformation(getNetwork(), tokenAddress)
            }?.let { underlyingToken ->
                LendingMarket(
                    id = "compound-ethereum-${it.address}",
                    network = getNetwork(),
                    protocol = getProtocol(),
                    name = it.name,
                    rate = getSupplyRate(compoundTokenContract = it),
                    address = it.address,
                    token = underlyingToken.toFungibleToken(),
                    marketSize = priceResource.calculatePrice(
                        PriceRequest(
                            underlyingToken.address,
                            getNetwork(),
                            it.cash.add(it.totalBorrows).toBigDecimal().divide(
                                BigDecimal.TEN.pow(underlyingToken.decimals),
                                18,
                                RoundingMode.HALF_UP
                            ),
                            TokenType.SINGLE
                        )
                    ).toBigDecimal(),
                    poolType = "compound-lendingpool",
                    balanceFetcher = BalanceFetcher(
                        it.address,
                        { user -> it.balanceOfMethod(user) },
                        { retVal ->
                            val tokenBalance = retVal[0].value as BigInteger
                            tokenBalance.times(it.exchangeRate).asEth(18).toBigInteger()
                        }
                    )
                )
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    fun getSupplyRate(compoundTokenContract: CompoundTokenContract): BigDecimal {
        val blocksPerDay = 6463
        val dailyRate =
            (compoundTokenContract.supplyRatePerBlock.toBigDecimal().divide(BigDecimal.TEN.pow(18)) * BigDecimal(
                blocksPerDay
            )) + BigDecimal.ONE

        return dailyRate.pow(365).minus(BigDecimal.ONE).times(BigDecimal.TEN.pow(4))
            .divide(BigDecimal.TEN.pow(4), 4, RoundingMode.HALF_UP)
    }

    override fun getProtocol(): Protocol {
        return Protocol.COMPOUND
    }

    override fun getNetwork(): Network {
        return Network.ETHEREUM
    }

    private fun getTokenContracts(): List<CompoundTokenContract> {
        val gateway = contractAccessorGateway.getGateway(getNetwork())
        return CompoundComptrollerContract(
            gateway,
            comptrollerABI,
            compoundService.getComptroller()
        ).getMarkets().map {
            CompoundTokenContract(
                gateway,
                cTokenABI,
                it
            )
        }
    }
}