package dev.needkirem.sample.data.dto

import dev.needkirem.cms.system.dto.CmsWidgetDto
import kotlinx.serialization.Serializable

@Serializable
data class ImageWidgetDto(
    override val id: String?,
    override val widgetType: String?,
    val description: String?,
    val imageUrl: String?,
) : CmsWidgetDto()