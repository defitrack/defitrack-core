package io.defitrack.protocol.claimable

import io.defitrack.abi.ABIResource
import io.defitrack.claimable.Claimable
import io.defitrack.claimable.ClaimableService
import io.defitrack.common.network.Network
import io.defitrack.evm.contract.ContractAccessorGateway
import io.defitrack.merkle.MerkleProofHelperService
import io.defitrack.protocol.HopAirdropService
import io.defitrack.protocol.Protocol
import io.defitrack.protocol.contract.HopTokenContract
import io.defitrack.token.ERC20Resource
import org.springframework.stereotype.Component

@Component
class HopAirdropClaimableService(
    private val erC20Resource: ERC20Resource,
    private val airdropService: HopAirdropService,
    private val hopAirdropTransactionBuilder: HopAirdropTransactionBuilder,
    private val accessorGateway: ContractAccessorGateway,
    private val merkleProofHelperService: MerkleProofHelperService,
    private val abiResource: ABIResource
) : ClaimableService {
    val hopTokenAddress = "0xc5102fe9359fd9a28f877a67e36b0f050d81a3cc"

    val hopToken by lazy {
        erC20Resource.getTokenInformation(getNetwork(), hopTokenAddress)
    }

    val hopTokenContract by lazy {
        HopTokenContract(
            accessorGateway.getGateway(getNetwork()),
            abiResource.getABI("hop/hopToken.json"),
            hopTokenAddress
        )
    }

    val merkleVerificationContract by lazy {
        MerkleProofHelperContract(
            accessorGateway.getGateway(Network.POLYGON),
            abiResource.getABI("defitrack/merkle/MerkleProofHelper.json"),
            merkleProofHelperService.getHelperAddress()
        )
    }

    override suspend fun claimables(address: String): List<Claimable> {
        return airdropService.getAirdrop(address)?.let {

            val verification = merkleVerificationContract.verify(
                "0x24007c66db4dfdaaa9a79e509286a8f164ea458846984c772272ff4aef327af3",
                it.balance,
                it.merkleProof,
                address
            )

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