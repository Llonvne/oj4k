package cn.llonvne

import cn.llonvne.domain.Problem
import cn.llonvne.domain.problem
import org.http4k.cloudnative.env.Environment
import org.http4k.core.Uri
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.jdbc.JdbcDatabase

object TestEnvironment {
    val env = Environment.defaults(
        Settings.DATABASE_URL of Uri.of("jdbc:h2:mem:quickstart;DB_CLOSE_DELAY=-1")
    )
}

fun main() {
    OnlineJudgeSystemServer(TestEnvironment.env).start().block()
}