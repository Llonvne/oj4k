package cn.llonvne.mapper.impl

import cn.llonvne.domain.User
import cn.llonvne.domain.user
import cn.llonvne.mapper.UserMapper
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.query.singleOrNull
import org.komapper.jdbc.JdbcDatabase

class UserMapperImpl(val database: JdbcDatabase) : UserMapper {

    companion object {
        private val e = Meta.user
    }

    override fun selectUserByNameOrNull(username: String): User? {
        return database.runQuery {
            QueryDsl.from(e).where {
                e.name eq username
            }.singleOrNull()
        }
    }
}