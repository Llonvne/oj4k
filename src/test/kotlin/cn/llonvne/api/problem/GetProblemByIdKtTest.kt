package cn.llonvne.api.problem

import cn.llonvne.api.requestTo
import cn.llonvne.domain.Problem
import cn.llonvne.mapper.FakeProblemMapper
import cn.llonvne.service.GetProblemByIdProcessorServiceProvider
import cn.llonvne.service.ServiceProvider
import io.kotest.matchers.should
import io.mockk.every
import io.mockk.mockk
import org.http4k.core.*
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.kotest.shouldHaveStatus
import org.junit.jupiter.api.Test


class GetProblemByIdKtTest {
    private val request: Request = GetProblemByIdDescriptor.requestTo()

    private val problems = listOf(Problem(1, "HelloWorld"))

    private val problemLen = Body.auto<Problem>().toLens()

    private val serviceProvider = mockk<ServiceProvider<GetProblemByIdProcessor>>()

    private val handler = GetProblemById(serviceProvider)

    init {
        every { serviceProvider.provide() } returns GetProblemByIdProcessor { id ->
            problems.find { it.id == id }
        }
    }

    @Test
    fun byId() {
        val problems = listOf(Problem(1, "HelloWorld"))
        val id = 1
        val req = request.uri(Uri.of(GetProblemByIdDescriptor.uri).appendToPath(id.toString()))
        val resp = handler(req)
        resp shouldHaveStatus Status.OK
        problemLen(resp) should matchProblemIgnoringTimestamps(problems.find { it.id == id }!!)
    }

    @Test
    fun illegalId() {
        val id = "wjdio"

        val req = request.uri(
            Uri.of(GetProblemByIdDescriptor.uri).appendToPath(id)
        )

        val resp = handler(req)

        resp shouldHaveStatus Status.NOT_FOUND
    }

    @Test
    fun idIsNull() {
        val resp = handler(request)
        resp shouldHaveStatus Status.NOT_FOUND
    }

    @Test
    fun idNotExist() {
        val id = -1

        val req = request.uri(
            Uri.of(GetProblemByIdDescriptor.uri).appendToPath(id.toString())
        )

        val resp = handler(req)

        resp shouldHaveStatus Status.NO_CONTENT
    }
}