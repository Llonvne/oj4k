package cn.llonvne.judger.api

import cn.llonvne.domain.ProblemTestcasesResult
import cn.llonvne.domain.SolutionStatus
import cn.llonvne.judger.task.CompileTaskOutput
import cn.llonvne.judger.task.ExecutableTaskOutput
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.http4k.core.Body
import org.http4k.format.KotlinxSerialization.auto

@Serializable
sealed interface JudgeRequest {
    val language: Language
    val code: String

    @Serializable
    @SerialName("Playground")
    data class PlaygroundClassicRequest(
        override val language: Language,
        override val code: String,
        val stdin: String
    ) : JudgeRequest

    @Serializable
    @SerialName("Problem")
    data class ProblemRequest(
        override val language: Language,
        override val code: String,
        val problemId: Int
    ) : JudgeRequest

    companion object {
        val lens = Body.auto<JudgeRequest>().toLens()
    }
}

@Serializable
sealed interface JudgeResult {

    @Serializable
    @SerialName("Raw")
    data class CompileAndExecutableResult(
        val compileTaskOutput: CompileTaskOutput,
        val executableTaskOutput: ExecutableTaskOutput
    ) : JudgeResult

    @Serializable
    @SerialName("ProblemNotFound")
    data class ProblemNotFound(val problemId: Int) : JudgeResult

    @Serializable
    @SerialName("Problem")
    data class DetailedProblemResult(
        val problemId: Int,
        val results: ProblemTestcasesResult,
        val solutionStatus: SolutionStatus
    ) : JudgeResult

    companion object {
        val lens = Body.auto<JudgeResult>().toLens()
    }
}

fun interface JudgeRequestProcessor {
    fun process(judgeRequest: JudgeRequest): JudgeResult
}