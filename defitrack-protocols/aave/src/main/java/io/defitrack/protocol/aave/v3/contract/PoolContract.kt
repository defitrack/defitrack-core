package io.defitrack.protocol.aave.v3.contract

import io.defitrack.abi.TypeUtils.Companion.toAddress
import io.defitrack.abi.TypeUtils.Companion.toUint16
import io.defitrack.abi.TypeUtils.Companion.toUint256
import io.defitrack.evm.contract.BlockchainGateway
import io.defitrack.evm.contract.EvmContract
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.Function
import java.math.BigInteger

class PoolContract(
    blockchainGateway: BlockchainGateway,
    abi: String,
    address: String
) : EvmContract(blockchainGateway, abi, address) {

    fun getSupplyFunction(asset: String, amount: BigInteger, onBehalfOf: String): Function {
        return createFunctionWithAbi(
            "supply",
            listOf(
                asset.toAddress(),
                amount.toUint256(),
                onBehalfOf.toAddress(),
                BigInteger.ZERO.toUint16()
            )
        )
    }

    suspend fun reservesList(): List<String> {
        return (readWithAbi("getReservesList", emptyList(), listOf(
            object : TypeReference<DynamicArray<Address>>() {}
        ))[0].value as List<Address>).map {
            it.value as String
        }
    }
}