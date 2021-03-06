package io.defitrack.farming

import io.defitrack.common.network.Network
import io.defitrack.farming.vo.TransactionPreparationVO
import io.defitrack.invest.PrepareInvestmentCommand
import io.defitrack.market.farming.FarmingMarketProvider
import io.defitrack.market.farming.vo.FarmingMarketVO
import io.defitrack.market.farming.vo.FarmingMarketVO.Companion.toVO
import io.defitrack.token.ERC20Resource
import io.defitrack.token.TokenType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(*["/staking", "/farming"])
class DefaultFarmingMarketRestController(
    private val farmingMarketProviders: List<FarmingMarketProvider>,
    private val erC20Resource: ERC20Resource
) {


    @GetMapping("/all-markets")
    fun stakingMarkets(
        @RequestParam("network") network: Network?
    ): List<FarmingMarketVO> {
        return farmingMarketProviders
            .filter { marketService ->
                network?.let {
                    marketService.getNetwork() == it
                } ?: true
            }.flatMap {
                it.getMarkets()
            }.map {
                it.toVO()
            }
    }

    @GetMapping(value = ["/markets"], params = ["token", "network"])
    fun searchByToken(
        @RequestParam("token") tokenAddress: String,
        @RequestParam("network") network: Network
    ): List<FarmingMarketVO>  = runBlocking(Dispatchers.IO) {
        farmingMarketProviders
            .filter {
                it.getNetwork() == network
            }.flatMap {
                it.getMarkets()
            }.filter {
                val token = erC20Resource.getTokenInformation(it.network, it.stakedToken.address.lowercase())
                if (token.type != TokenType.SINGLE) {
                    it.stakedToken.address.lowercase() == tokenAddress.lowercase() ||
                            token.underlyingTokens.any { underlyingToken ->
                                underlyingToken.address.lowercase() == tokenAddress.lowercase()
                            }
                } else {
                    it.stakedToken.address.lowercase() == tokenAddress.lowercase()
                }
            }.map {
                it.toVO()
            }
    }

    @GetMapping(value = ["/markets/{marketId}"])
    fun getById(
        @PathVariable("marketId") id: String,
    ): ResponseEntity<FarmingMarketVO> {
        return getStakingMarketById(id)?.let {
            ResponseEntity.ok(it.toVO())
        } ?: ResponseEntity.notFound().build()
    }

    private fun getStakingMarketById(
        id: String
    ) = farmingMarketProviders
        .flatMap {
            it.getMarkets()
        }.firstOrNull {
            it.id == id
        }

    @PostMapping(value = ["/markets/{id}/enter"])
    fun prepareInvestment(
        @PathVariable("id") id: String,
        @RequestBody prepareInvestmentCommand: PrepareInvestmentCommand
    ): ResponseEntity<TransactionPreparationVO> = runBlocking {
        getStakingMarketById(id)?.investmentPreparer?.prepare(prepareInvestmentCommand)?.let { transactions ->
            ResponseEntity.ok(
                TransactionPreparationVO(
                    transactions
                )
            )
        } ?: ResponseEntity.badRequest().build()
    }
}