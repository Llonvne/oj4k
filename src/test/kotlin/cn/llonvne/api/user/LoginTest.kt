package cn.llonvne.api.user

import cn.llonvne.api.requestTo
import cn.llonvne.api.security.SecurityToken
import cn.llonvne.domain.Token
import cn.llonvne.domain.User
import cn.llonvne.mapper.FakeUserMapper
import cn.llonvne.service.LoginProcessorServiceProvider
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.kotest.shouldHaveBody
import org.http4k.kotest.shouldHaveStatus
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginTest {
    private val users = mutableListOf(User(name = "Admin", password = "Admin"))
    val handler = Login(
        LoginProcessorServiceProvider(
            FakeUserMapper(users)
        )
    )
    private val request = LoginDescriptor.requestTo()

    @Test
    fun testLoginSuccess() {
        val resp = handler(
            request.with(
                LoginRequest.lens of LoginRequest.LoginViaUsernameAndPassword("Admin", "Admin")
            )
        )

        resp shouldHaveStatus Status.OK
        resp shouldHaveBody Json.encodeToString(SecurityToken(Token.UserToken("Admin")))
    }

    @Test
    fun testPasswordWrong() {
        val resp = handler(
            request.with(
                LoginRequest.lens of LoginRequest.LoginViaUsernameAndPassword("Admin", "wrongPassword")
            )
        )
        resp shouldHaveStatus Status.UNAUTHORIZED
        resp shouldHaveBody "username or password incorrect"
    }

    @Test
    fun testUserNotFound() {
        val resp = handler(
            request.with(
                LoginRequest.lens of LoginRequest.LoginViaUsernameAndPassword("userNotExist", "Admin")
            )
        )
        resp shouldHaveStatus Status.UNAUTHORIZED
        resp shouldHaveBody "username or password incorrect"
    }
}