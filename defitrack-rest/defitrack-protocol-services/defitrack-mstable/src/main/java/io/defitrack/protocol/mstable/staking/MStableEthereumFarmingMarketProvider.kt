package io.defitrack.protocol.mstable.staking

import io.defitrack.abi.ABIResource
import io.defitrack.common.network.Network
import io.defitrack.evm.contract.BlockchainGatewayProvider
import io.defitrack.market.farming.FarmingMarketProvider
import io.defitrack.market.farming.domain.FarmingMarket
import io.defitrack.market.farming.domain.FarmingPositionFetcher
import io.defitrack.protocol.FarmType
import io.defitrack.protocol.Protocol
import io.defitrack.protocol.mstable.MStableEthereumService
import io.defitrack.protocol.mstable.contract.MStableEthereumBoostedSavingsVaultContract
import io.defitrack.token.ERC20Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class MStableEthereumFarmingMarketProvider(
    private val blockchainGatewayProvider: BlockchainGatewayProvider,
    private val mStableEthereumService: MStableEthereumService,
    private val abiResource: ABIResource,
    private val erC20Resource: ERC20Resource
) : FarmingMarketProvider() {

    val boostedSavingsVaultABI by lazy {
        abiResource.getABI("mStable/BoostedSavingsVault.json")
    }

    override suspend fun fetchMarkets(): List<FarmingMarket> = coroutineScope {
        val gateway = blockchainGatewayProvider.getGateway(getNetwork())

        mStableEthereumService.getBoostedSavingsVaults().map {
            MStableEthereumBoostedSavingsVaultContract(
                gateway,
                boostedSavingsVaultABI,
                it
            )
        }.map { contract ->
            async {
                try {
                    toStakingMarket(contract)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    null
                }
            }
        }.awaitAll().filterNotNull()
    }

    private suspend fun toStakingMarket(contract: MStableEthereumBoostedSavingsVaultContract): FarmingMarket {
        val stakingToken = erC20Resource.getTokenInformation(getNetwork(), contract.stakingToken())
        val rewardsToken = erC20Resource.getTokenInformation(getNetwork(), contract.rewardsToken())
        return create(
            identifier = contract.address,
            name = contract.name(),
            stakedToken = stakingToken.toFungibleToken(),
            rewardTokens = listOf(
                rewardsToken.toFungibleToken()
            ),
            vaultType = "mstable-boosted-savings-vault",
            balanceFetcher = FarmingPositionFetcher(
                address = contract.address,
                { user -> contract.rawBalanceOfFunction(user) }
            ),
            farmType = FarmType.VAULT
        )
    }

    override fun getProtocol(): Protocol {
        return Protocol.MSTABLE
    }

    override fun getNetwork(): Network {
        return Network.ETHEREUM
    }
}