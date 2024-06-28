package cn.llonvne.api.problem

import cn.llonvne.api.requestTo
import cn.llonvne.domain.Problem
import cn.llonvne.mapper.FakeProblemMapper
import cn.llonvne.service.GetProblemListQueriesProcessorServiceProvider
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Body
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.kotest.shouldHaveStatus
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetProblemListTest {
    private val request = GetProblemListDescriptor.requestTo()
    private val problems = listOf(
        Problem(id = 1, name = "Llonvne")
    )
    private val service = GetProblemList(
        GetProblemListQueriesProcessorServiceProvider(
            FakeProblemMapper(
                mutableListOf(
                    Problem(1, "Llonvne")
                )
            )
        )
    )
    private val respLens = Body.auto<List<Problem>>().toLens()

    @Test
    fun testQueriesSerialized() {
        Json.encodeToString<GetProblemListQueries>(GetProblemListQueries.ByName("123")) shouldBe
                """{"type":"ByName","name":"123"}"""
    }

    private fun matchProblemsIgnoringTimestamps(expected: List<Problem>): Matcher<List<Problem>> =
        object : Matcher<List<Problem>> {
            override fun test(value: List<Problem>): MatcherResult {
                val result = value.size == expected.size &&
                        value.zip(expected).all { (actual, expected) ->
                            actual.id == expected.id &&
                                    actual.name == expected.name &&
                                    actual.version == expected.version
                        }

                return MatcherResult(
                    result,
                    { "List $value should be equal to $expected ignoring timestamps" },
                    { "List $value should not be equal to $expected ignoring timestamps" }
                )
            }
        }

    @Test
    fun testByIds() {
        val resp = service(request.with(GetProblemListQueries.lens of GetProblemListQueries.ByIds(listOf(1))))
        val actual = respLens(resp)
        actual should matchProblemsIgnoringTimestamps(listOf(problems.find { it.id == 1 }!!))
    }

    @Test
    fun testByIdsWithJson() {
        val resp = service(
            request.body("""{"type":"ByName","name":"Llonvne"}""")
        )
        val actual = respLens(resp)
        actual should matchProblemsIgnoringTimestamps(listOf(Problem(1, "Llonvne")))
    }

    @Test
    fun testInvalidJson() {
        val resp = service(
            request.body("""{"type":"yName","name":"Llonvne"}""")
        )
        resp shouldHaveStatus Status.BAD_REQUEST
    }

    @Test
    fun testIdsResultEmpty() {
        val resp = service(
            request.with(GetProblemListQueries.lens of GetProblemListQueries.ByIds(listOf(-1)))
        )

        resp shouldHaveStatus Status.NO_CONTENT
    }
}


