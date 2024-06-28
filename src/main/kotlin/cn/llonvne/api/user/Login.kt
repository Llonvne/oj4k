package cn.llonvne.api.user

import cn.llonvne.api.ApiDescriptor
import cn.llonvne.api.security.SecurityToken
import cn.llonvne.domain.Token
import cn.llonvne.domain.User
import cn.llonvne.service.ServiceProvider
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.http4k.contract.ContractRoute
import org.http4k.contract.bindContract
import org.http4k.core.*
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.lens.LensFailure

data object LoginDescriptor : ApiDescriptor {
    override val method: Method = Method.POST
    override val uri: String = "/login"
}

fun interface LoginProcessor {
    fun login(loginRequest: LoginRequest): LoginResult
}

sealed interface LoginResult {
    val username: String

    data class LoginUserNotFound(override val username: String) : LoginResult

    data class WrongPassword(override val username: String) : LoginResult

    data class OK(override val username: String, val user: User) : LoginResult
}

@Serializable
sealed interface LoginRequest {
    @Serializable
    @SerialName("password")
    data class LoginViaUsernameAndPassword(val username: String, val password: String) : LoginRequest

    @Serializable
    @SerialName("token")
    data class LoginViaToken(val token: String) : LoginRequest

    companion object {
        val lens = Body.auto<LoginRequest>().toLens()
    }
}

@Suppress("FunctionName")
fun Login(serviceProvider: ServiceProvider<LoginProcessor>): ContractRoute =
    LoginDescriptor.uri bindContract LoginDescriptor.method to handler@{ req ->

        val service = serviceProvider.provide()

        val request = try {
            LoginRequest.lens(req)
        } catch (e: LensFailure) {
            return@handler Response(Status.BAD_REQUEST)
        }

        when (val resp = service.login(request)) {
            is LoginResult.LoginUserNotFound -> {
                Response(Status.UNAUTHORIZED).body("username or password incorrect")
            }

            is LoginResult.OK -> {
                Response(Status.OK).with(
                    Body.auto<SecurityToken>().toLens() of SecurityToken(Token.UserToken(resp.username))
                )
            }

            is LoginResult.WrongPassword -> {
                Response(Status.UNAUTHORIZED).body("username or password incorrect")
            }
        }
    }