package cn.llonvne

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.hamkrest.hasStatus
import org.http4k.testing.ApprovalTest
import org.http4k.testing.Approver
import org.http4k.testing.assertApproved
import org.http4k.testing.hasApprovedContent
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApprovalTest::class)
class ExampleApprovalTest {
    private val app: HttpHandler = { Response(OK).body("hello world") }

    @Test
    fun `check response content`(approver: Approver) {
        approver.assertApproved(app(Request(Method.GET, "/url")))
    }

    @Test
    fun `check response content with expected status`(approver: Approver) {
        approver.assertApproved(app(Request(Method.GET, "/url")), OK)
    }

    @Test
    fun `check request content`(approver: Approver) {
        approver.assertApproved(Request(Method.GET, "/url").body("foobar"))
    }

    @Test
    fun `combine approval with hamkrest matcher`(approver: Approver) {
        assertThat(app(Request(Method.GET, "/url")), hasStatus(OK).and(approver.hasApprovedContent()))
    }
}