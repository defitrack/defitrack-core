package io.defitrack.evm.contract

import io.defitrack.evm.contract.BlockchainGateway.Companion.toAddress
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import java.math.BigInteger

open class ERC20Contract(
    blockchainGateway: BlockchainGateway,
    abi: String,
    address: String
) :
    EvmContract(blockchainGateway, abi, address) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun balanceOfMethod(address: String): Function {
        return createFunction(
            "balanceOf",
            inputs = listOf(address.toAddress()),
            outputs = listOf(
                TypeReference.create(Uint256::class.java)
            )
        )
    }

    fun balanceOf(address: String): BigInteger {
        return read(
            "balanceOf",
            inputs = listOf(address.toAddress()),
            outputs = listOf(
                TypeReference.create(Uint256::class.java)
            )
        )[0].value as BigInteger
    }

    val name by lazy {
        try {
            read("name")[0].value as String
        } catch (ex: Exception) {
            "unknown"
        }
    }

    val symbol by lazy {
        val read = read("symbol")
        if (read.isEmpty()) {
            "unknown"
        } else {
            read[0].value as String
        }
    }

    val decimals by lazy {
        val read = read("decimals")
        if (read.isEmpty()) {
            18
        }  else {
            (read[0].value as BigInteger).toInt()
        }
    }
}