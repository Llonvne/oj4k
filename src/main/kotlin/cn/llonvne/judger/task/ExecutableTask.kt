package cn.llonvne.judger.task

import cn.llonvne.judger.api.Cmd
import cn.llonvne.judger.api.GoJudgeFile
import cn.llonvne.judger.api.Result
import kotlinx.serialization.Serializable

data class ExecutableTaskInput(
    val executableFileId: String,
    val stdin: String,
    val cpuLimit: Long = 10240000,
    val memoryLimit: Long = 10240000,
    val procLimit: Int = 50
) {
    fun getCmd(): Cmd {
        return Cmd(
            args = listOf("a"),
            env = listOf("PATH=/usr/bin:/bin"),
            files = listOf(
                GoJudgeFile.MemoryFile(content = stdin),
                GoJudgeFile.Collector("stdout", 10240),
                GoJudgeFile.Collector("stderr", 10240)
            ),
            cpuLimit = cpuLimit,
            memoryLimit = memoryLimit,
            procLimit = procLimit,
            copyIn = mapOf(
                "a" to GoJudgeFile.PreparedFile(executableFileId)
            )
        )
    }
}

@Serializable
data class ExecutableTaskOutput(
    private val result: Result
) {

    val status get() = result.status

    val err get() = result.error

    val time get() = result.time

    val memory get() = result.memory

    val stderr get() = result.files?.getOrDefault("stderr", null)

    val stdout get() = result.files?.getOrDefault("stdout", null)
}

class ExecutableTask(
    private val runner: RequestProcessor
) : Task<ExecutableTaskInput, ExecutableTaskOutput> {
    override fun run(input: ExecutableTaskInput): ExecutableTaskOutput {
        return ExecutableTaskOutput(
            runner.runCmd(input.getCmd())
        )
    }
}