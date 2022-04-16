package dev.needkirem.domain

import dev.needkirem.cms.system.model.WidgetModel

data class CmsPage(
    val version: String,
    val widgets: List<WidgetModel>
)