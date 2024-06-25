package cn.llonvne.mapper

import cn.llonvne.domain.Problem

class FakeProblemMapper(
    private val problems: MutableList<Problem> = mutableListOf()
) : ProblemMapper {
    override fun selectByIdOrNull(id: Int): Problem? {
        return problems.find { it.id == id }
    }

    override fun selectByIds(ids: List<Int>): List<Problem> {
        val idSet = ids.toSet()
        return problems.filter { it.id in idSet }
    }

    override fun selectByNameContain(content: String): List<Problem> {
        return problems.filter { it.name.contains(content) }
    }
}