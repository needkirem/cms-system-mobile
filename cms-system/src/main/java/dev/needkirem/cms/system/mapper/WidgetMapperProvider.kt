package dev.needkirem.cms.system.mapper

import dev.needkirem.cms.system.dto.CmsWidgetDto
import dev.needkirem.cms.system.model.WidgetModel

abstract class WidgetMapperProvider {

    abstract fun get(widgetType: String): WidgetMapper<CmsWidgetDto, WidgetModel>
}