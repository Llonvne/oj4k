package cn.llonvne.api.user

import cn.llonvne.api.requestTo
import cn.llonvne.domain.User
import cn.llonvne.mapper.FakeUserMapper
import cn.llonvne.service.LoginProcessorServiceProvider
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.kotest.shouldHaveBody
import org.http4k.kotest.shouldHaveStatus
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginTest {
    val users = mutableListOf(User(name = "Admin", password = "Admin"))
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
        resp shouldHaveBody "Login successful"
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