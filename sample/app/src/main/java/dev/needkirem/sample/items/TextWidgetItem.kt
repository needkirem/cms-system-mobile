package dev.needkirem.sample.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import dev.needkirem.domain.TextWidget
import dev.needkirem.sample.R
import dev.needkirem.sample.databinding.ItemTextWidgetBinding

class TextWidgetItem(
    private val model: TextWidget
) : AbstractBindingItem<ItemTextWidgetBinding>() {

    override val type: Int = R.id.item_text_widget

    override fun bindView(binding: ItemTextWidgetBinding, payloads: List<Any>) {
        binding.text.text = model.text
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemTextWidgetBinding {
        return ItemTextWidgetBinding.inflate(inflater, parent, false)
    }
}