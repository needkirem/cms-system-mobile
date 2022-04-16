package dev.needkirem.sample.data.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
data class CmsPageDto(
    val version: String?,
    val widgets: JsonArray?,
)