package cn.llonvne.mapper.impl

import cn.llonvne.domain.Problem
import cn.llonvne.domain.problem
import cn.llonvne.mapper.ProblemMapper
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.operator.desc
import org.komapper.core.dsl.query.singleOrNull
import org.komapper.jdbc.JdbcDatabase

class ProblemMapperImpl(val database: JdbcDatabase) : ProblemMapper {
    override fun selectByIdOrNull(id: Int): Problem? {
        return database.runQuery {
            QueryDsl.from(e).where {
                e.id eq id
            }.singleOrNull()
        }
    }

    override fun selectByIds(ids: List<Int>): List<Problem> {
        return database.runQuery {
            QueryDsl.from(e).where {
                e.id inList ids
            }.orderBy(e.createdAt.desc()).limit(500)
        }
    }

    override fun selectByNameContain(content: String): List<Problem> {
        return database.runQuery {
            QueryDsl.from(e).where {
                e.name contains content
            }.orderBy(e.createdAt.desc()).limit(500)
        }
    }

    companion object {
        private val e = Meta.problem
    }
}