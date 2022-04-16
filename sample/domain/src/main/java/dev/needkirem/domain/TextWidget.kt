package dev.needkirem.domain

import dev.needkirem.cms.system.model.WidgetModel

data class TextWidget(
    val id: String,
    val text: String,
) : WidgetModel