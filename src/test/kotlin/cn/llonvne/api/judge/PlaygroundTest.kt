package cn.llonvne.api.judge

import cn.llonvne.api.requestTo
import cn.llonvne.domain.*
import cn.llonvne.fkDb
import cn.llonvne.judger.api.JudgeRequest
import cn.llonvne.judger.api.JudgeRequest.*
import cn.llonvne.judger.api.Language
import cn.llonvne.judger.task.GoJudgeRequestProcessor
import cn.llonvne.mapper.FakeProblemMapper
import cn.llonvne.service.DatabaseInitializer
import cn.llonvne.service.LanguageCompileTaskInputProviderImpl
import cn.llonvne.service.JudgeRequestProcessorServiceProvider
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.with
import org.junit.jupiter.api.Test

class PlaygroundTest {

    init {
        DatabaseInitializer(fkDb).init()
    }

    private val problemMapper = FakeProblemMapper(
        mutableListOf(
            Problem(
                id = 1, "A + B", problemTestcasesRaw =
                Json.encodeToString(
                    ProblemTestcases(
                        listOf(
                            PlainTestcase("1 2", "3")
                        ),
                        SolutionStatusDeterminer.PassAllTestcase
                    )
                )
            )
        )
    )

    private val playground = Judge(
        JudgeRequestProcessorServiceProvider(
            GoJudgeRequestProcessor(),
            LanguageCompileTaskInputProviderImpl(),
            problemMapper
        )
    )


    @Test
    fun sampleTest() {
        println(
            playground(
                JudgeApiDescriptor.requestTo().with(
                    JudgeRequest.lens of ProblemRequest(
                        Language.C11,
                        """
                            #include "iostream";

                            int main() {
                                int a, b;
                                std::cin >> a >> b;
                                std::cout << a + b;
                            
                        """.trimIndent(),
                        1
                    )
                )
            )
        )
    }
}