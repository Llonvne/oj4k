package cn.llonvne.api.security

import cn.llonvne.api.ApiDescriptor
import cn.llonvne.api.user.LoginDescriptor
import cn.llonvne.domain.Token
import cn.llonvne.serial.Base64
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.http4k.contract.security.BearerAuthSecurity
import org.http4k.contract.security.Security
import org.http4k.core.Filter
import org.http4k.core.Request
import org.http4k.core.then
import org.http4k.lens.RequestContextLens

typealias SecurityToken = Base64<Token>

class OJSecurity(
    val key: RequestContextLens<SecurityToken>, private val allowedApiDescriptor: List<ApiDescriptor> = listOf(
        LoginDescriptor
    )
) : Security {

    private val bearerAuthSecurity = BearerAuthSecurity(key, {
        try {
            Json.decodeFromString<SecurityToken>(it)
        } catch (_: SerializationException) {
            null
        }
    })

    fun token(request: Request): SecurityToken {
        return key(request)
    }


    override val filter: Filter = Filter { next ->
        { req ->
            if (allowedApiDescriptor.any {
                    it.uri == req.uri.path && it.method == req.method
                }) {
                next(req)
            } else {
                bearerAuthSecurity.filter.then(next)(req)
            }
        }
    }
}