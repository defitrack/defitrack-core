package io.defitrack.merkle

import org.springframework.stereotype.Component

@Component
class MerkleProofHelperService {
    fun getHelperAddress(): String {
        return "0x7df89515bc267f1c428613334782b57b08620eea" //polygon
    }
}