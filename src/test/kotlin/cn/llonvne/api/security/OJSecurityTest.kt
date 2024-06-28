package cn.llonvne.api.security

import cn.llonvne.api.problem.GetProblemListDescriptor
import cn.llonvne.api.requestTo
import cn.llonvne.api.user.LoginDescriptor
import org.http4k.core.*
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.kotest.shouldHaveStatus
import org.http4k.lens.RequestContextKey
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.junit.jupiter.api.Test

class OJSecurityTest {
    private val contexts = RequestContexts()
    private val ojSecurity = OJSecurity(RequestContextKey.required(contexts))
    private val testApp = routes("/login" bind Method.POST to {
        Response(OK)
    })

    @Test
    fun `test allowed uri without token`() {
        val app = ojSecurity.filter.then(testApp)
        app(LoginDescriptor.requestTo()) shouldHaveStatus OK
        app(GetProblemListDescriptor.requestTo()) shouldHaveStatus UNAUTHORIZED
    }
}