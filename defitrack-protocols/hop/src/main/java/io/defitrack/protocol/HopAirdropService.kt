package io.defitrack.protocol

import com.google.gson.JsonParser
import io.github.reactivecircus.cache4k.Cache
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.springframework.stereotype.Service
import java.math.BigInteger

@Service
class HopAirdropService(private val httpClient: HttpClient) {

    val baseUrl = "https://raw.githubusercontent.com/defitrack/data/master/protocols/hop/airdrops/"

    val cache = Cache.Builder().build<String, List<HopAirdrop>>()

    suspend fun getAirdrop(address: String): HopAirdrop? {
        val addressInitials = getAddressInitials(address)

        val entries = cache.get(addressInitials) {
            val asString = httpClient.get("$baseUrl/$addressInitials.json").bodyAsText()
            JsonParser.parseString(asString).asJsonObject["entries"].asJsonObject.entrySet().map {
                HopAirdrop(
                    it.key,
                    it.value.asJsonObject["balance"].asBigInteger
                )
            }
        }
        return entries.find {
            it.address.lowercase() == address.lowercase()
        }
    }

    private fun getAddressInitials(address: String): String {
        return address.removePrefix("0x").substring(0..1)
    }

    class HopAirdrop(
        val address: String,
        val balance: BigInteger
    )
}