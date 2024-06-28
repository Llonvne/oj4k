package cn.llonvne.api

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.routing.bind

interface ApiDescriptor {
    val method: Method
    val uri: String
}