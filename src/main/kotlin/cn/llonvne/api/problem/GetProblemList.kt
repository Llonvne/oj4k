@file:Suppress("FunctionName")

package cn.llonvne.api.problem

import cn.llonvne.api.ApiDescriptor
import cn.llonvne.api.problem.GetProblemListQueries.Companion.lens
import cn.llonvne.domain.Problem
import cn.llonvne.service.ServiceProvider
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.http4k.asString
import org.http4k.contract.ContractRoute
import org.http4k.contract.bindContract
import org.http4k.core.*
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.lens.LensFailure


data object GetProblemListDescriptor : ApiDescriptor {
    override val method: Method = Method.POST
    override val uri: String = ProblemUriRoot
}

@Serializable
sealed interface GetProblemListQueries {
    @Serializable
    @SerialName("ByIds")
    data class ByIds(val ids: List<Int>) : GetProblemListQueries

    @Serializable
    @SerialName("ByName")
    data class ByName(val name: String) : GetProblemListQueries

    companion object {
        val lens = Body.auto<GetProblemListQueries>().toLens()
    }
}

fun interface GetProblemListQueryProcessor {
    fun process(configure: GetProblemListQueries): List<Problem>
}

fun GetProblemList(
    serviceProvider: ServiceProvider<GetProblemListQueryProcessor>,
): ContractRoute {
    return GetProblemListDescriptor.uri bindContract GetProblemListDescriptor.method to handler@{ it: Request ->
        val processor = serviceProvider.provide()

        val query: GetProblemListQueries = try {
            lens(it)
        } catch (e: LensFailure) {
            return@handler Response(Status.BAD_REQUEST).body("unsupported query json:${it.body.payload.asString()}")
        }

        val result = processor.process(query)

        return@handler if (result.isEmpty()) {
            Response(Status.NO_CONTENT)
        } else {
            Response(Status.OK).with(
                Body.auto<List<Problem>>().toLens() of result
            )
        }
    }
}