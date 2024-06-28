package cn.llonvne.domain

import cn.llonvne.judger.api.Status
import cn.llonvne.judger.task.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SolutionStatus {
    Accepted, WrongAnswer, MemoryLimitExceed, TimeLimitExceed, RuntimeError, CompileError
}

@Serializable
data class ProblemTestcases(
    val testcases: List<ProblemTestcase>,
    val statusDeterminer: SolutionStatusDeterminer
) {
    fun passAll(interactor: TestcaseInteractor): ProblemTestcasesResult {
        return ProblemTestcasesResult(testcases.map { testcase ->
            testcase.pass(interactor)
        }, statusDeterminer)
    }
}

@Serializable
sealed interface TestcaseInteractor {

    @Serializable
    data class CompileAndExecutableOutput(
        val compileResult: CompileTaskOutput, val executableResult: ExecutableTaskOutput?
    )

    fun run(stdin: String): CompileAndExecutableOutput

    class RequestTestcaseInteractor(
        private val input: CompileTaskInput,
        private val requestProcessor: RequestProcessor,
    ) : TestcaseInteractor {
        override fun run(stdin: String): CompileAndExecutableOutput {

            val compileResp = CompileTask(requestProcessor).run(input)

            if (compileResp.status == Status.NonzeroExitStatus) {
                return CompileAndExecutableOutput(compileResp, null)
            }

            val outputFileId = compileResp.outputFileId ?: return CompileAndExecutableOutput(compileResp, null)

            val executableResult = ExecutableTask(requestProcessor).run(
                ExecutableTaskInput(outputFileId, stdin)
            )

            return CompileAndExecutableOutput(compileResp, executableResult)
        }
    }
}


@Serializable
sealed interface ProblemTestcase {
    fun pass(interactor: TestcaseInteractor): ProblemTestcaseResult
}

@Serializable
data class PlainTestcase(
    val input: String, val expectOutput: String
) : ProblemTestcase {
    override fun pass(interactor: TestcaseInteractor): ProblemTestcaseResult {

        val result = interactor.run(input)

        if (result.executableResult == null) {
            return ProblemTestcaseResult.CompileError(result.compileResult)
        }

        val actualOutput = result.executableResult.stdout

        return ProblemTestcaseResult.PlainTestcaseResult(input, expectOutput, actualOutput ?: "<stdout is null>")
    }
}

@Serializable
sealed interface ProblemTestcaseResult {
    @Serializable
    @SerialName("CompileError")
    data class CompileError(val compileResult: CompileTaskOutput) : ProblemTestcaseResult {
        override fun result(): SolutionStatus {
            return SolutionStatus.CompileError
        }
    }

    fun result(): SolutionStatus

    @Serializable
    @SerialName("PlainTestcase")
    data class PlainTestcaseResult(
        val input: String, val expectOutput: String, val actualOutput: String
    ) : ProblemTestcaseResult {
        override fun result(): SolutionStatus {
            return if (expectOutput == actualOutput) {
                SolutionStatus.Accepted
            } else {
                SolutionStatus.WrongAnswer
            }
        }
    }
}

@Serializable
data class ProblemTestcasesResult(
    val result: List<ProblemTestcaseResult>,
    val statusDeterminer: SolutionStatusDeterminer
)

@Serializable
sealed interface SolutionStatusDeterminer {

    fun result(result: List<ProblemTestcaseResult>): SolutionStatus

    @Serializable
    @SerialName("PassAllTestcase")
    data object PassAllTestcase : SolutionStatusDeterminer {
        override fun result(result: List<ProblemTestcaseResult>): SolutionStatus {
            return if (result.all { it.result() == SolutionStatus.Accepted }) {
                SolutionStatus.Accepted
            } else {
                SolutionStatus.WrongAnswer
            }
        }
    }
}
