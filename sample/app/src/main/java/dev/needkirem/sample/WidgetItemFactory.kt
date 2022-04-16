package dev.needkirem.sample

import com.mikepenz.fastadapter.IItem
import dev.needkirem.cms.system.model.WidgetModel
import dev.needkirem.domain.ImageWidget
import dev.needkirem.domain.TextWidget
import dev.needkirem.sample.items.ImageWidgetItem
import dev.needkirem.sample.items.TextWidgetItem

class WidgetItemFactory {

    fun create(widget: WidgetModel): IItem<*>? {
        return when (widget) {
            is TextWidget -> createTextWidgetItem(widget)
            is ImageWidget -> createImageWidgetItem(widget)
            else -> null
        }
    }

    private fun createTextWidgetItem(model: TextWidget): TextWidgetItem {
        return TextWidgetItem(model)
    }

    private fun createImageWidgetItem(model: ImageWidget): ImageWidgetItem {
        return ImageWidgetItem(model)
    }
}