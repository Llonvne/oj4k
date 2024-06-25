package cn.llonvne.api

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.routing.bind

interface ApiDescriptor {
    val method: Method
    val uri: String

    fun routing(httpHandler: HttpHandler) = uri bind method to httpHandler

    companion object {
        fun of(uri: String, method: Method): ApiDescriptor {
            return object : ApiDescriptor {
                override val method: Method = method
                override val uri: String = uri
            }
        }
    }
}