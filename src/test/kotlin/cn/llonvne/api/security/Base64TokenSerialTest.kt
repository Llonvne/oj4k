package cn.llonvne.api.security

import cn.llonvne.domain.Token
import cn.llonvne.serial.Base64
import kotlinx.serialization.encodeToString
import org.http4k.core.*
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.filter.ServerFilters
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.format.KotlinxSerialization.json
import org.http4k.kotest.shouldHaveBody
import org.http4k.kotest.shouldHaveStatus
import org.http4k.lens.RequestContextKey
import org.http4k.lens.bearerAuth
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.junit.jupiter.api.Test

class Base64TokenSerialTest {
    private val contexts = RequestContexts()
    private val key = RequestContextKey.required<Base64<Token>>(contexts)
    private val security = OJSecurity(key)

    private val app = ServerFilters.InitialiseRequestContext(contexts).then(
        routes("/secure" bind Method.GET to security.filter.then {
            val token = security.token(it)
            Response(OK).with(
                Body.auto<Base64<Token>>().toLens() of token
            )
        })
    )

    @Test
    fun `test user token`() {
        val token = Base64(Token.UserToken("123") as Token)

        val base64Token = json.encodeToString(token)

        val request = Request(Method.GET, "/secure").bearerAuth(base64Token)
        val response = app(request)

        response shouldHaveStatus OK
        response shouldHaveBody base64Token
    }

    @Test
    fun `test invalid token`() {
        val invalidToken = Base64("12345678")
        val based64InvaildToken = json.encodeToString(invalidToken)
        val request = Request(Method.GET, "/secure").bearerAuth(based64InvaildToken)

        val response = app(request)
        response shouldHaveStatus UNAUTHORIZED
    }
}