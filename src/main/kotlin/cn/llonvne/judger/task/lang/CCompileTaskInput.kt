package cn.llonvne.judger.task.lang

import cn.llonvne.judger.api.Cmd
import cn.llonvne.judger.api.GoJudgeFile
import cn.llonvne.judger.api.RequestType
import cn.llonvne.judger.task.CompileTaskInput

class CCompileTaskInput(
    code: String,
    private val cmdBuilder: (Cmd) -> Unit = {}
) : CompileTaskInput {
    private val compileCmd = Cmd(
        args = listOf("/usr/bin/g++", "a.cc", "-o", "a"),
        env = listOf("PATH=/usr/bin:/bin"),
        files = listOf(
            GoJudgeFile.MemoryFile(""),
            GoJudgeFile.Collector("stdout", 10240),
            GoJudgeFile.Collector("stderr", 10240)
        ),
        cpuLimit = 10000000000,
        memoryLimit = 104857600,
        procLimit = 50,
        copyIn = mapOf(
            "a.cc" to GoJudgeFile.MemoryFile(
                code
            )
        ),
        copyOut = listOf("stdout", "stderr"),
        copyOutCached = listOf("a.cc", "a")
    )


    override fun getCmd(): Cmd {
        return compileCmd.apply(cmdBuilder)
    }

    override val sourceCodeFilename: String = "a.cc"
    override val outputFilename: String = "a"
}