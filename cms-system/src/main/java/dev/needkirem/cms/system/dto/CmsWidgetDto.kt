package dev.needkirem.cms.system.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
abstract class CmsWidgetDto {
    @SerialName("id") abstract val id: String
    @SerialName("widgetType") abstract val widgetType: String
}