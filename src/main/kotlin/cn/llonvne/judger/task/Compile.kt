package cn.llonvne.judger.task

import cn.llonvne.judger.api.Cmd
import cn.llonvne.judger.api.Result
import kotlinx.serialization.Serializable

class CompileException(msg: String) : Exception(msg)

interface CompileTaskInput {
    val sourceCodeFilename: String
    val outputFilename: String

    fun getCmd(): Cmd
}
@Serializable
data class CompileTaskOutput(
    val sourceCodeFilename: String,
    val outputFilename: String,
    val result: Result
) {

    val status get() = result.status

    val err get() = result.error

    val time get() = result.time

    val memory get() = result.memory

    val stderr get() = result.files?.getOrDefault("stderr", null)

    val stdin get() = result.fileIds?.getOrDefault("stdin", null)

    val sourceCodeId get() = result.fileIds?.getOrDefault(sourceCodeFilename, null)

    val outputFileId get() = result.fileIds?.getOrDefault(outputFilename, null)
}

class CompileTask(
    private val processor: RequestProcessor
) : Task<CompileTaskInput, CompileTaskOutput> {
    override fun run(input: CompileTaskInput): CompileTaskOutput {
        return CompileTaskOutput(
            sourceCodeFilename = input.sourceCodeFilename,
            outputFilename = input.outputFilename,
            processor.runCmd(input.getCmd())
        )
    }
}