package dev.needkirem.cms.system.mapper

import dev.needkirem.cms.system.model.WidgetModel
import dev.needkirem.cms.system.utils.getString
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonObject

class CmsSystemWidgetMapper(
    private val jsonWidgetMapperProvider: JsonWidgetMapperProvider,
    private val modelWidgetMapperProvider: WidgetMapperProvider,
) {

    fun map(jsonWidgets: JsonArray): List<WidgetModel> {
        return jsonWidgets.mapNotNull { jsonElement ->
            val widgetType = jsonElement.jsonObject.getString("widgetType").orEmpty()
            val jsonMapper = jsonWidgetMapperProvider.get(widgetType)
            val modelMapper = modelWidgetMapperProvider.get(widgetType)
            try {
                val dto = checkNotNull(jsonMapper?.map(jsonElement.jsonObject))
                modelMapper.map(dto)
            } catch (throwable: Throwable) {
                null
            }
        }
    }
}