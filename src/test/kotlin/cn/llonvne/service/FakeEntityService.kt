package cn.llonvne.service

import org.komapper.jdbc.JdbcDatabase

open class FakeEntityService<Meta, E>(
    override val database: JdbcDatabase,
    override val e: Meta
) : EntityService<Meta, E>

