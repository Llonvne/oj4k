package cn.llonvne.domain

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.komapper.annotation.*

@KomapperEntity
@Serializable
data class Problem(
    @KomapperId @KomapperAutoIncrement
    val id: Int = 0,
    val name: String,
    val problemTestcasesRaw: String = emptyTestcases,
    @KomapperVersion
    val version: Int = 0,
    @KomapperCreatedAt
    val createdAt: LocalDateTime = java.time.LocalDateTime.now().toKotlinLocalDateTime(),
    @KomapperUpdatedAt
    val updatedAt: LocalDateTime = java.time.LocalDateTime.now().toKotlinLocalDateTime(),
) {

    val problemTestcases: ProblemTestcases = Json.decodeFromString(problemTestcasesRaw)

    companion object {
        private val emptyTestcases =
            Json.encodeToString<ProblemTestcases>(ProblemTestcases(listOf(), SolutionStatusDeterminer.PassAllTestcase))
    }
}