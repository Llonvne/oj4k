@file:Suppress("FunctionName")

package cn.llonvne.api.problem

import cn.llonvne.api.ApiDescriptor
import cn.llonvne.domain.Problem
import cn.llonvne.service.ServiceProvider
import org.http4k.contract.div
import org.http4k.core.*
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.lens.Path
import org.http4k.lens.int


data object GetProblemByIdDescriptor : ApiDescriptor {
    override val method: Method = Method.GET
    override val uri: String = ProblemUriRoot
}

fun interface GetProblemByIdProcessor {
    fun process(id: Int): Problem?
}

private val idPath = Path.int().of("id")

fun GetProblemById(serviceProvider: ServiceProvider<GetProblemByIdProcessor>) =
    GetProblemByIdDescriptor.uri / idPath bindContract GetProblemByIdDescriptor.method to { id ->
        {
            val service = serviceProvider.provide()
            val result = service.process(id)
            if (result == null) {
                Response(Status.NO_CONTENT)
            } else {
                Response(Status.OK).with(
                    Body.auto<Problem>().toLens() of result
                )
            }
        }
    }