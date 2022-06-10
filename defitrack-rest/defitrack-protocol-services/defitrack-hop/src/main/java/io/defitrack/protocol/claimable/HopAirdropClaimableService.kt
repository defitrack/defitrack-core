package io.defitrack.protocol.claimable

import io.defitrack.claimable.Claimable
import io.defitrack.claimable.ClaimableService
import io.defitrack.common.network.Network
import io.defitrack.protocol.HopAirdropService
import io.defitrack.protocol.Protocol
import io.defitrack.token.ERC20Resource
import org.springframework.stereotype.Component

@Component
class HopAirdropClaimableService(
    private val erC20Resource: ERC20Resource,
    private val airdropService: HopAirdropService,
    private val hopAirdropTransactionBuilder: HopAirdropTransactionBuilder
) : ClaimableService {
    val hopTokenAddress = "0xc5102fe9359fd9a28f877a67e36b0f050d81a3cc"

    val hopToken by lazy {
        erC20Resource.getTokenInformation(getNetwork(), hopTokenAddress)
    }

    override suspend fun claimables(address: String): List<Claimable> {
        return airdropService.getAirdrop(address)?.let {
            listOf(
                Claimable(
                    id = "hop-airdrop",
                    name = "Hop Token Airdrop",
                    address = hopTokenAddress,
                    type = "hop-airdrop",
                    protocol = getProtocol(),
                    network = getNetwork(),
                    claimableToken = hopToken.toFungibleToken(),
                    amount = it.balance,
                    claimTransaction = hopAirdropTransactionBuilder.buildTransactions(it)
                )
            )
        } ?: emptyList()
    }

    override fun getProtocol(): Protocol {
        return Protocol.HOP
    }

    override fun getNetwork(): Network {
        return Network.ETHEREUM
    }
}