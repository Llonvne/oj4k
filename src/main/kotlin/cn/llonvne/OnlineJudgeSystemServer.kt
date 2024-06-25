@file:Suppress("FunctionName")

package cn.llonvne

import cn.llonvne.Settings.DATABASE_URL
import cn.llonvne.Settings.PORT
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.events.AutoMarshallingEvents
import org.http4k.format.KotlinxSerialization
import org.http4k.lens.int
import org.http4k.lens.uri
import org.http4k.server.Http4kServer
import org.http4k.server.Undertow
import org.http4k.server.asServer
import java.time.Clock

fun OnlineJudgeSystemServer(environment: Environment): Http4kServer {
    return OnlineJudgeSystem(
        Clock.systemUTC(),
        AutoMarshallingEvents(KotlinxSerialization),
        DATABASE_URL(environment),
    ).asServer(Undertow(port = PORT(environment)))
}

object Settings {
    val PORT = EnvironmentKey.int().defaulted("PORT", 9000, "OnlineJudge Server Port")
    val DATABASE_URL = EnvironmentKey.uri().required("DATABASE_URL", "Database Url")
}

