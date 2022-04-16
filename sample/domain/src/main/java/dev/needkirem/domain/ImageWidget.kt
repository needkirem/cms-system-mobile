package dev.needkirem.domain

import dev.needkirem.cms.system.model.WidgetModel

data class ImageWidget(
    val id: String,
    val description: String,
    val imageUrl: String,
) : WidgetModel