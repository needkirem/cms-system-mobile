package dev.needkirem.cms.system.models

import kotlinx.serialization.Serializable

@Serializable
data class CmsPage(
    val widgets: List<CmsWidgetBase>,
)