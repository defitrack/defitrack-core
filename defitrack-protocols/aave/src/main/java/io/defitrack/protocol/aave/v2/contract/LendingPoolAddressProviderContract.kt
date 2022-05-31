package io.defitrack.protocol.aave.v2.contract

import io.defitrack.evm.contract.BlockchainGateway
import io.defitrack.evm.contract.EvmContract
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address

class LendingPoolAddressProviderContract(blockchainGateway: BlockchainGateway, abi: String, address: String) :
    EvmContract(
        blockchainGateway, abi, address
    ) {

    fun lendingPoolAddress(): String {
        return readWithAbi(
            "getLendingPool",
            emptyList(),
            listOf(TypeReference.create(Address::class.java))
        )[0].value as String
    }

    fun priceOracleAddress(): String {
        return readWithAbi(
            "getPriceOracle",
            emptyList(),
            listOf(TypeReference.create(Address::class.java))
        )[0].value as String
    }
}