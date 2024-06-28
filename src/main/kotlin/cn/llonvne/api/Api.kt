@file:Suppress("FunctionName")

package cn.llonvne.api

import cn.llonvne.api.judge.Judge
import cn.llonvne.api.problem.GetProblemById
import cn.llonvne.api.problem.GetProblemList
import cn.llonvne.api.security.OJSecurity
import cn.llonvne.api.security.SecurityToken
import cn.llonvne.api.user.Login
import cn.llonvne.judger.task.RequestProcessor
import cn.llonvne.mapper.ProblemMapper
import cn.llonvne.mapper.UserMapper
import cn.llonvne.service.*
import org.http4k.contract.contract
import org.http4k.events.Events
import org.http4k.lens.RequestContextLens
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.routes

fun Api(
    tokenKey: RequestContextLens<SecurityToken>,
    userMapper: UserMapper,
    problemMapper: ProblemMapper,
    requestProcessor: RequestProcessor,
    events: Events
): RoutingHttpHandler = routes(
    contract {
        security = OJSecurity(tokenKey)

        routes += GetProblemById(GetProblemByIdProcessorServiceProvider(problemMapper))
        routes += GetProblemList(GetProblemListQueriesProcessorServiceProvider(problemMapper))

        routes += Login(LoginProcessorServiceProvider(userMapper))

        routes += Judge(
            JudgeRequestProcessorServiceProvider(
                requestProcessor,
                LanguageCompileTaskInputProviderImpl(),
                problemMapper
            )
        )
    }
)

