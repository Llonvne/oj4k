@file:Suppress("FunctionName")

package cn.llonvne

import cn.llonvne.api.Api
import cn.llonvne.api.security.SecurityToken
import cn.llonvne.mapper.impl.ProblemMapperImpl
import cn.llonvne.mapper.impl.UserMapperImpl
import org.http4k.core.HttpHandler
import org.http4k.core.RequestContexts
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.events.Events
import org.http4k.filter.HandleRemoteRequestFailed
import org.http4k.filter.ServerFilters
import org.http4k.lens.RequestContextKey
import org.http4k.routing.routes
import org.komapper.jdbc.JdbcDatabase
import java.time.Clock

fun OnlineJudgeSystem(
    clock: Clock,
    events: Events,
    databaseUrl: Uri,
): HttpHandler {
    val database = JdbcDatabase(databaseUrl.toString())
    val userMapper = UserMapperImpl(database)
    val problemMapper = ProblemMapperImpl(database)
    val contexts = RequestContexts()
    val tokenKey = RequestContextKey.required<SecurityToken>(contexts)
    val app = routes(Api(tokenKey, userMapper, problemMapper, events = events))
    return ServerFilters.CatchAll()
        .then(ServerFilters.InitialiseRequestContext(contexts))
        .then(ServerFilters.RequestTracing())
        .then(ServerFilters.HandleRemoteRequestFailed())
        .then(app)
}
