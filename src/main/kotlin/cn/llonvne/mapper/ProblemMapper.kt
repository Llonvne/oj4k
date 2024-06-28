package cn.llonvne.mapper

import cn.llonvne.domain.Problem

interface ProblemMapper {
    fun selectByIdOrNull(id: Int): Problem?

    fun selectByIds(ids: List<Int>): List<Problem>

    fun selectByNameContain(content: String): List<Problem>

    fun insert(problem: Problem): Problem
}