package io.defitrack.protocol.yearn

import io.defitrack.common.network.Network
import io.defitrack.market.farming.FarmingMarketProvider
import io.defitrack.market.farming.domain.FarmingMarket
import io.defitrack.protocol.FarmType
import io.defitrack.protocol.Protocol
import io.defitrack.token.ERC20Resource
import org.springframework.stereotype.Component

@Component
class YearnFarmingMarketProvider(
    private val yearnService: YearnService,
    private val erC20Resource: ERC20Resource
) : FarmingMarketProvider() {
    override suspend fun fetchMarkets(): List<FarmingMarket> {
        return yearnService.provideYearnV2Vaults().map {
            val stakedtoken =
                erC20Resource.getTokenInformation(getNetwork(), it.token.id).toFungibleToken()
            create(
                name = it.shareToken.name,
                identifier = it.id,
                stakedToken = stakedtoken,
                rewardTokens = listOf(stakedtoken),
                vaultType = "yearn-v2",
                farmType = FarmType.VAULT
            )
        }
    }

    override fun getProtocol(): Protocol {
        TODO("Not yet implemented")
    }

    override fun getNetwork(): Network {
        TODO("Not yet implemented")
    }
}