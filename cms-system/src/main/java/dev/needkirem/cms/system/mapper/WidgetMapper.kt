package dev.needkirem.cms.system.mapper

import dev.needkirem.cms.system.dto.CmsWidgetDto
import dev.needkirem.cms.system.model.WidgetModel

abstract class WidgetMapper<in T : CmsWidgetDto, out M : WidgetModel> {

    abstract fun map(dto: T): M
}