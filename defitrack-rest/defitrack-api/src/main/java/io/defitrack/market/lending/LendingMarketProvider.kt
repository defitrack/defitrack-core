package io.defitrack.market.lending

import io.defitrack.invest.MarketProvider
import io.defitrack.market.farming.domain.InvestmentPreparer
import io.defitrack.market.lending.domain.BalanceFetcher
import io.defitrack.market.lending.domain.LendingMarket
import io.defitrack.token.FungibleToken
import java.math.BigDecimal

abstract class LendingMarketProvider : MarketProvider<LendingMarket>() {

    fun create(
        identifier: String,
        name: String,
        token: FungibleToken,
        poolType: String,
        marketSize: BigDecimal? = null,
        rate: BigDecimal? = null,
        balanceFetcher: BalanceFetcher? = null,
        investmentPreparer: InvestmentPreparer? = null
    ): LendingMarket {
        return LendingMarket(
            id = "lnd_${getNetwork().slug}-${getProtocol().slug}-${identifier}",
            network = getNetwork(),
            protocol = getProtocol(),
            name = name,
            token = token,
            marketSize = marketSize,
            rate = rate,
            poolType = poolType,
            balanceFetcher = balanceFetcher,
            investmentPreparer = investmentPreparer
        )
    }
}