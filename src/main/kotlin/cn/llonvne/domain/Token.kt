package cn.llonvne.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Token {
    @Serializable
    @SerialName("TEST")
    data object TestToken : Token

    @Serializable
    @SerialName("User")
    data class UserToken(val username: String) : Token

    @Serializable
    @SerialName("None")
    data object NoToken : Token
}
