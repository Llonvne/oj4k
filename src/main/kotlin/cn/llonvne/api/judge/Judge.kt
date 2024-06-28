package cn.llonvne.api.judge

import cn.llonvne.api.ApiDescriptor
import cn.llonvne.judger.api.JudgeRequest
import cn.llonvne.judger.api.JudgeRequestProcessor
import cn.llonvne.judger.api.JudgeResult
import cn.llonvne.service.ServiceProvider
import org.http4k.contract.ContractRoute
import org.http4k.contract.bindContract
import org.http4k.core.*
import org.http4k.lens.LensFailure

data object JudgeApiDescriptor : ApiDescriptor {
    override val method: Method = Method.POST
    override val uri: String = "/judge"
}


@Suppress("functionName")
fun Judge(serviceProvider: ServiceProvider<JudgeRequestProcessor>): ContractRoute =
    JudgeApiDescriptor.uri bindContract JudgeApiDescriptor.method to handler@{ req: Request ->
        val playReq = try {
            JudgeRequest.lens(req)
        } catch (e: LensFailure) {
            return@handler Response(Status.BAD_REQUEST)
        }
        val service = serviceProvider.provide()
        val resp = service.process(playReq)
        Response(Status.OK).with(
            JudgeResult.lens of resp
        )
    }