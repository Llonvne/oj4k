package cn.llonvne.judger

import cn.llonvne.judger.task.*
import cn.llonvne.judger.task.lang.CCompileTaskInput
import org.junit.jupiter.api.Test

class TaskTest {
    @Test
    fun sample() {
        val processor = GoJudgeRequestProcessor()

        val compileResp = CompileTask(processor).run(
            CCompileTaskInput(
                """
                #include <map>

                #include "iostream";

                int main() {
                    auto mapper = std::map<int, std::string>{
                        {1, "123"}
                    };

                    std::cout << mapper[1];
                }

            """.trimIndent()
            )
        )

        val runResp = ExecutableTask(processor).run(
            ExecutableTaskInput(compileResp.outputFileId!!, "")
        )

        println(runResp)
    }
}