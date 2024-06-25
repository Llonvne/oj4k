package cn.llonvne

import cn.llonvne.domain.Problem
import cn.llonvne.domain.problem
import org.http4k.cloudnative.env.Environment
import org.http4k.core.Uri
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.jdbc.JdbcDatabase

fun onGivenProblems(
    database: JdbcDatabase,
    problems: List<Problem> = listOf(
        Problem(1, "Llonvne"),
        Problem(2, "Lcosvle")
    ), action: (JdbcDatabase) -> Unit
) {
    val e = Meta.problem
    database.runQuery {
        QueryDsl.create(e)
    }

    database.runQuery {
        QueryDsl.insert(e).batch(problems)
    }

    action(database)

    database.runQuery {
        QueryDsl.drop(e)
    }
}

fun main() {
    val env = Environment.defaults(
        Settings.DATABASE_URL of Uri.of("jdbc:h2:mem:quickstart;DB_CLOSE_DELAY=-1")
    )
    OnlineJudgeSystemServer(env).start().block()
}