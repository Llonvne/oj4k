package cn.llonvne.service

import cn.llonvne.api.problem.GetProblemByIdProcessor
import cn.llonvne.mapper.ProblemMapper

class GetProblemByIdProcessorServiceProvider(
    private val problemMapper: ProblemMapper
) : ServiceProvider<GetProblemByIdProcessor> {
    override fun provide() = GetProblemByIdProcessor {
        problemMapper.selectByIdOrNull(it)
    }
}