package io.defitrack.balance.l1

import io.defitrack.balance.BalanceService
import io.defitrack.balance.TokenBalance
import io.defitrack.common.network.Network
import io.defitrack.fantom.config.FantomContractAccessor
import io.defitrack.fantom.config.FantomGateway
import io.defitrack.token.ERC20Resource
import org.springframework.stereotype.Service
import org.web3j.protocol.core.DefaultBlockParameterName
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

@Service
class FantomBalanceService(
    private val fantomGateway: FantomGateway,
    private val fantomContractAccessor: FantomContractAccessor,
    private val erC20Service: ERC20Resource
) : BalanceService {

    override fun getNetwork(): Network = Network.FANTOM

    override fun getNativeBalance(address: String): BigDecimal =
        fantomGateway.web3j().ethGetBalance(address, DefaultBlockParameterName.LATEST).send().balance
            .toBigDecimal().divide(
                BigDecimal.TEN.pow(18), 4, RoundingMode.HALF_UP
            )

    override fun getTokenBalances(user: String): List<TokenBalance> {
        val tokenAddresses = erC20Service.getAllTokens(getNetwork()).map {
            it.address
        }

        if (tokenAddresses.isEmpty()) {
            return emptyList()
        }

        return erC20Service.getBalancesFor(user, tokenAddresses, fantomContractAccessor)
            .mapIndexed { i, balance ->
                if (balance > BigInteger.ZERO) {
                    val token = erC20Service.getERC20(getNetwork(), tokenAddresses[i])
                    TokenBalance(
                        address = token.address,
                        amount = balance,
                        decimals = token.decimals,
                        symbol = token.symbol,
                        name = token.name,
                        network = getNetwork(),
                    )
                } else {
                    null
                }
            }.filterNotNull()
    }


    override fun nativeTokenName(): String {
        return "FTM"
    }
}