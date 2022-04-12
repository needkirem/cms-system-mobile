package dev.needkirem.cms.system.mapper

import dev.needkirem.cms.system.dto.CmsWidgetDto
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

abstract class JsonWidgetMapper {

    protected val json: Json = Json {
        ignoreUnknownKeys = true
    }

    abstract fun map(jsonObject: JsonObject): CmsWidgetDto?
}