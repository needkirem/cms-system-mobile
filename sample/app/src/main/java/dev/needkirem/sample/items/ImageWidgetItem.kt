package dev.needkirem.sample.items

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import dev.needkirem.domain.ImageWidget
import dev.needkirem.sample.R
import dev.needkirem.sample.databinding.ItemImageWidgetBinding

class ImageWidgetItem(
    private val model: ImageWidget
) : AbstractBindingItem<ItemImageWidgetBinding>() {

    override val type: Int = R.id.item_image_widget

    override fun bindView(binding: ItemImageWidgetBinding, payloads: List<Any>) {
        binding.image.load(model.imageUrl)
        binding.description.text = model.description
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemImageWidgetBinding {
        return ItemImageWidgetBinding.inflate(inflater, parent, false)
    }
}