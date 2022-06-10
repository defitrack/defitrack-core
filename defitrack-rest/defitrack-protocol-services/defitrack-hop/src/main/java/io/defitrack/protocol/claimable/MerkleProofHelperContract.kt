package io.defitrack.protocol.claimable

import io.defitrack.evm.contract.BlockchainGateway
import io.defitrack.evm.contract.BlockchainGateway.Companion.bool
import io.defitrack.evm.contract.BlockchainGateway.Companion.toAddress
import io.defitrack.evm.contract.BlockchainGateway.Companion.toBytes32
import io.defitrack.evm.contract.BlockchainGateway.Companion.toUint256
import io.defitrack.evm.contract.BlockchainGateway.Companion.uint256
import io.defitrack.evm.contract.EvmContract
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.generated.Bytes32
import java.math.BigInteger

class MerkleProofHelperContract(
    blockchainGateway: BlockchainGateway,
    abi: String, address: String
) : EvmContract(
    blockchainGateway, abi, address
) {

    fun verify(
        merkleRoot: String,
        amount: BigInteger,
        merkleProof: List<String>,
        owner: String
    ): MerkleVerification {
        val retVal = read(
            "verify",
            inputs = listOf(
                merkleRoot.toBytes32(),
                amount.toUint256(),
                DynamicArray(
                    Bytes32::class.java, merkleProof.map { it.toBytes32() },
                ),
                owner.toAddress()
            ),
            outputs = listOf(
                bool(),
                uint256()
            )
        )

        return MerkleVerification(
            retVal[0].value as Boolean,
            retVal[1].value as BigInteger,
        )
    }

    class MerkleVerification(
        val valid: Boolean,
        val index: BigInteger
    )


}