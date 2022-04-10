package dev.needkirem.cms.system.mapper

import dev.needkirem.cms.system.models.CmsWidgetBase
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

abstract class WidgetMapper {

    protected val json: Json = Json {
        ignoreUnknownKeys = true
    }

    abstract fun map(jsonObject: JsonObject): CmsWidgetBase?
}

