package cn.llonvne.domain

import cn.llonvne.domain.primitive.HiddenStringSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.komapper.annotation.*
import org.komapper.core.type.ClobString

@KomapperEntity
@KomapperTable(name = "users")
@Serializable
data class User(
    @KomapperId @KomapperAutoIncrement val id: Int = 0,
    val name: String,
    @Serializable(with = HiddenStringSerializer::class) val password: String,
    @KomapperColumn(alternateType = ClobString::class) val tokenRaw: String = NoTokenRaw,
    @KomapperVersion val version: Int = 0,
    @KomapperCreatedAt val createdAt: LocalDateTime = java.time.LocalDateTime.now().toKotlinLocalDateTime(),
    @KomapperUpdatedAt val updatedAt: LocalDateTime = java.time.LocalDateTime.now().toKotlinLocalDateTime()
) {
    companion object {
        private val NoTokenRaw = Json.encodeToString<Token>(Token.NoToken)
    }

    val token: Token = Json.decodeFromString(tokenRaw)
}