package cn.llonvne.api

import org.http4k.core.Request

fun ApiDescriptor.requestTo(): Request =
    Request(method, uri)
