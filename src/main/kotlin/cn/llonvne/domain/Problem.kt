package cn.llonvne.domain

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.Serializable
import org.komapper.annotation.*

@KomapperEntity
@Serializable
data class Problem(
    @KomapperId @KomapperAutoIncrement
    val id: Int = 0,
    val name: String,
    @KomapperVersion
    val version: Int = 0,
    @KomapperCreatedAt
    val createdAt: LocalDateTime = java.time.LocalDateTime.now().toKotlinLocalDateTime(),
    @KomapperUpdatedAt
    val updatedAt: LocalDateTime = java.time.LocalDateTime.now().toKotlinLocalDateTime()
)