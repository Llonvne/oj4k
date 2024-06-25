package cn.llonvne.api.problem

import cn.llonvne.domain.Problem
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult

fun matchProblemIgnoringTimestamps(expected: Problem) = object : Matcher<Problem> {
    override fun test(value: Problem): MatcherResult {
        val match = expected.copy(
            createdAt = value.createdAt,
            updatedAt = value.updatedAt
        ) == value

        return MatcherResult(
            match,
            { "Expected problem: $expected but was $value, ignoring timestamps" },
            { "Problem matched: $value" }
        )
    }
}