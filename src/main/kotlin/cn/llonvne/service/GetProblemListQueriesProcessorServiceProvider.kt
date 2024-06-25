package cn.llonvne.service

import cn.llonvne.api.problem.GetProblemListQueries
import cn.llonvne.api.problem.GetProblemListQueryProcessor
import cn.llonvne.mapper.ProblemMapper

class GetProblemListQueriesProcessorServiceProvider(
    private val problemMapper: ProblemMapper
) : ServiceProvider<GetProblemListQueryProcessor> {
    override fun provide(): GetProblemListQueryProcessor {
        return GetProblemListQueryProcessor {
            when (it) {
                is GetProblemListQueries.ByName -> problemMapper.selectByNameContain(it.name)

                is GetProblemListQueries.ByIds -> problemMapper.selectByIds(it.ids)
            }
        }
    }
}