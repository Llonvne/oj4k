package cn.llonvne.judger.task

import cn.llonvne.judger.api.*
import org.http4k.client.OkHttp
import org.http4k.core.*
import org.http4k.format.KotlinxSerialization.auto


interface Task<I, R> {
    fun run(input: I): R
}

interface RequestProcessor {
    fun runRequest(request: RequestType.Request): List<Result>
}

fun RequestProcessor.runCmd(cmd: Cmd): Result {
    return runRequest(
        RequestType.Request(
            cmd = listOf(cmd)
        )
    ).first()
}


class GoJudgeRequestProcessor(
    private val goJudgeUri: String = "http://localhost:5050/run",
    private val httpHandler: HttpHandler = OkHttp(),
) : RequestProcessor {

    override fun runRequest(request: RequestType.Request): List<Result> {
        val req = Request(Method.POST, goJudgeUri).with(reqLens of request)
        val resp = httpHandler(req)
        val result = respLens(resp)
        return result
    }

    companion object {
        private val reqLens = Body.auto<RequestType.Request>().toLens()
        private val respLens = Body.auto<List<Result>>().toLens()
    }
}