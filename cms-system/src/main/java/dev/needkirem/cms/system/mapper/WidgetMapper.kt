package dev.needkirem.cms.system.mapper

import dev.needkirem.cms.system.dto.CmsWidgetDto
import dev.needkirem.cms.system.model.WidgetModel

abstract class WidgetMapper {

    abstract fun map(dto: CmsWidgetDto): WidgetModel
}