package io.defitrack.protocol.contract

import io.defitrack.evm.contract.BlockchainGateway
import io.defitrack.evm.contract.BlockchainGateway.Companion.toAddress
import io.defitrack.evm.contract.BlockchainGateway.Companion.toBytes32
import io.defitrack.evm.contract.BlockchainGateway.Companion.toUint256
import io.defitrack.evm.contract.ERC20Contract
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Bytes32
import java.math.BigInteger

class HopTokenContract(
    blockchainGateway: BlockchainGateway,
    abi: String,
    address: String
) : ERC20Contract(
    blockchainGateway, abi, address
) {

    fun claimTokensFunction(
        amount: BigInteger,
        delegate: String,
        proof: List<String>,
    ): Function {
        return createFunction(
            "claimTokens",
            listOf(
                amount.toUint256(),
                delegate.toAddress(),
                DynamicArray(Bytes32::class.java, proof.map { it.toBytes32() })
            ),
            emptyList()
        )
    }
}