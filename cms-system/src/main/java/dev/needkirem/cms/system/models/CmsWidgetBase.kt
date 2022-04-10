package dev.needkirem.cms.system.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
abstract class CmsWidgetBase {
    @SerialName("id") abstract val id: String
    @SerialName("widgetType") abstract val widgetType: String
}