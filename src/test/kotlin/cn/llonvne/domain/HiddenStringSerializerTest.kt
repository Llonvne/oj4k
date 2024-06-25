package cn.llonvne.domain


import cn.llonvne.domain.primitive.HiddenStringSerializer
import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test


class HiddenStringSerializerTest {
    @Serializable
    data class Account(
        val name: String,
        val password: @Serializable(with = HiddenStringSerializer::class) String
    )

    @Test
    fun testPasswordSerialize() {

        val account = Account("123", "456")
        val serialized = Json.encodeToString(account)
        serialized shouldBe """{"name":"123","password":"<HIDDEN>"}""".trimIndent()

        val deserialized = Json.decodeFromString<Account>(
            """
            {
            "name": "123",
            "password": "456"
            }
        """.trimIndent()
        )

        deserialized shouldBe Account("123", "456")
    }
}