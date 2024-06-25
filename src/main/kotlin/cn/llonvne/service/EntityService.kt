package cn.llonvne.service

import org.komapper.jdbc.JdbcDatabase

interface EntityService<META, E> {
    val database: JdbcDatabase
    val e: META
}

fun interface ServiceProvider<E> {
    fun provide(): E
}
