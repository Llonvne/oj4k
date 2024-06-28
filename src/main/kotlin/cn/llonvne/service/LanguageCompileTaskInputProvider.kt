package cn.llonvne.service

import cn.llonvne.judger.api.JudgeRequest
import cn.llonvne.judger.api.Language
import cn.llonvne.judger.task.CompileTaskInput
import cn.llonvne.judger.task.lang.CCompileTaskInput

class LanguageCompileTaskInputProviderImpl : LanguageCompileTaskInputProvider {
    override fun provide(language: Language, request: JudgeRequest): CompileTaskInput {
        return when (language) {
            Language.C11 -> CCompileTaskInput(request.code)
        }
    }
}