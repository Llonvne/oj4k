@file:Suppress("FunctionName")

package cn.llonvne

import cn.llonvne.Settings.DATABASE_URL
import cn.llonvne.Settings.PORT
import cn.llonvne.Settings.REQUEST_PROCESSOR_URL
import org.http4k.client.OkHttp
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.core.Uri
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
        REQUEST_PROCESSOR_URL(environment),
        OkHttp()
    ).asServer(Undertow(port = PORT(environment)))
}

object Settings {
    val PORT = EnvironmentKey.int().defaulted("PORT", 9000, "OnlineJudge Server Port")
    val DATABASE_URL = EnvironmentKey.uri().required("DATABASE_URL", "Database Url")
    val REQUEST_PROCESSOR_URL =
        EnvironmentKey.uri().defaulted("REQUEST_PROCESSOR_URL", Uri.of("http://localhost:5050/run"), "GoJudge Url")
}

