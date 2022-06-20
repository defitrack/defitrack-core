package io.defitrack.protocol.claimable

import io.defitrack.abi.ABIResource
import io.defitrack.common.network.Network
import io.defitrack.evm.contract.BlockchainGatewayProvider
import io.defitrack.protocol.HopAirdropService
import io.defitrack.protocol.contract.HopTokenContract
import io.defitrack.transaction.PreparedTransaction
import org.springframework.stereotype.Service

@Service
class HopAirdropTransactionBuilder(
    gateway: BlockchainGatewayProvider,
    abiResource: ABIResource
) {

    private val blockchainGateway = gateway.getGateway(Network.ETHEREUM)
    val hopToken = "0xc5102fe9359fd9a28f877a67e36b0f050d81a3cc"

    val hopTokenContract = HopTokenContract(
        blockchainGateway,
        abiResource.getABI("hop/hopToken.json"),
        hopToken
    )

    fun buildTransactions(airdrop: HopAirdropService.HopAirdrop): List<PreparedTransaction> {
        return listOf(
            PreparedTransaction(
                hopTokenContract.claimTokensFunction(
                    airdrop.balance,
                    "0x2b888954421b424c5d3d9ce9bb67c9bd47537d12",
                    airdrop.merkleProof
                ),
                hopToken
            )
        )
    }

}