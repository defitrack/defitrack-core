package io.defitrack.protocol.contract

import io.defitrack.evm.contract.BlockchainGateway
import io.defitrack.evm.contract.ERC20Contract

class HopLpTokenContract(
    blockchainGateway: BlockchainGateway,
    abi: String,
    address: String
) : ERC20Contract(blockchainGateway, abi, address) {

    suspend fun swap(): String {
        return readWithAbi("swap")[0].value as String
    }
}