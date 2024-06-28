package cn.llonvne.service

import cn.llonvne.domain.TestcaseInteractor
import cn.llonvne.judger.api.JudgeRequest
import cn.llonvne.judger.api.JudgeRequest.PlaygroundClassicRequest
import cn.llonvne.judger.api.JudgeRequestProcessor
import cn.llonvne.judger.api.JudgeResult
import cn.llonvne.judger.api.JudgeResult.CompileAndExecutableResult
import cn.llonvne.judger.api.Language
import cn.llonvne.judger.task.*
import cn.llonvne.mapper.ProblemMapper

fun interface LanguageCompileTaskInputProvider {
    fun provide(language: Language, request: JudgeRequest): CompileTaskInput
}

class JudgeRequestProcessorServiceProvider(
    private val requestProcessor: RequestProcessor,
    private val languageCompileTaskInputProvider: LanguageCompileTaskInputProvider,
    private val problemMapper: ProblemMapper
) : ServiceProvider<JudgeRequestProcessor> {
    override fun provide(): JudgeRequestProcessor {
        return JudgeRequestProcessor {
            when (it) {
                is PlaygroundClassicRequest -> PlaygroundClassicRequestProcessor(
                    requestProcessor, languageCompileTaskInputProvider
                ).process(it)

                is JudgeRequest.ProblemRequest -> ProblemRequestProcessor(
                    requestProcessor, languageCompileTaskInputProvider, problemMapper
                ).process(it)
            }
        }
    }
}

private interface JudgeProcessor<I : JudgeRequest> {
    fun process(input: I): JudgeResult
}

private class PlaygroundClassicRequestProcessor(
    private val requestProcessor: RequestProcessor,
    private val languageCompileTaskInputProvider: LanguageCompileTaskInputProvider
) : JudgeProcessor<PlaygroundClassicRequest> {
    override fun process(input: PlaygroundClassicRequest): JudgeResult {
        val language = input.language

        val compileTaskInput = languageCompileTaskInputProvider.provide(language, input)

        val compileTask = CompileTask(requestProcessor)

        val compileResp = compileTask.run(compileTaskInput)

        val executableTask = ExecutableTask(requestProcessor)

        val executableResp = executableTask.run(ExecutableTaskInput(compileResp.outputFileId!!, input.stdin))

        return CompileAndExecutableResult(compileResp, executableResp)
    }
}

private class ProblemRequestProcessor(
    private val requestProcessor: RequestProcessor,
    private val languageCompileTaskInputProvider: LanguageCompileTaskInputProvider,
    private val problemMapper: ProblemMapper
) : JudgeProcessor<JudgeRequest.ProblemRequest> {
    override fun process(input: JudgeRequest.ProblemRequest): JudgeResult {
        val problem =
            problemMapper.selectByIdOrNull(input.problemId) ?: return JudgeResult.ProblemNotFound(input.problemId)
        val testcases = problem.problemTestcases

        val compileTaskInput = languageCompileTaskInputProvider.provide(input.language, input)

        val result = testcases.passAll(
            TestcaseInteractor.RequestTestcaseInteractor(compileTaskInput, requestProcessor)
        )

        return JudgeResult.DetailedProblemResult(input.problemId, result, result.statusDeterminer.result(result.result))
    }
}