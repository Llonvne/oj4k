package cn.llonvne.service

import cn.llonvne.domain.Problem
import cn.llonvne.domain._Problem
import cn.llonvne.domain.problem
import org.komapper.core.dsl.Meta
import org.komapper.jdbc.JdbcDatabase

class ProblemService(
    override val database: JdbcDatabase,
    override val e: _Problem = Meta.problem
) : EntityService<_Problem, Problem>
