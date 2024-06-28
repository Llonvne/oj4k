package cn.llonvne.service

import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.jdbc.JdbcDatabase

class DatabaseInitializer(private val database: JdbcDatabase) {
    fun init() {
        database.runQuery {
            QueryDsl.create(Meta.all())
        }
    }
}