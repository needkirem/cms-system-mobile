package dev.needkirem.cms.system.utils

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

fun JsonObject.getString(key: String): String? {
    return if (get(key) is JsonPrimitive) {
        (get(key) as JsonPrimitive).content
    } else {
        null
    }
}

fun JsonObject.getArray(key: String): List<JsonElement> {
    return if (get(key) is JsonArray) {
        (get(key) as JsonArray).toList()
    } else {
        emptyList()
    }
}