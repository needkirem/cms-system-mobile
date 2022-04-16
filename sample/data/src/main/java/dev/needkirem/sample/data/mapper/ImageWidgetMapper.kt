package dev.needkirem.sample.data.mapper

import dev.needkirem.cms.system.annotation.CmsWidgetMapper
import dev.needkirem.cms.system.mapper.WidgetMapper
import dev.needkirem.domain.ImageWidget
import dev.needkirem.sample.data.dto.ImageWidgetDto

@CmsWidgetMapper(
    widgetType = "ImageWidget",
    dto = ImageWidgetDto::class,
    model = ImageWidget::class,
)
class ImageWidgetMapper : WidgetMapper<ImageWidgetDto, ImageWidget>() {

    override fun map(dto: ImageWidgetDto): ImageWidget {
        val id = requireNotNull(dto.id)
        val description = dto.description.orEmpty()
        val imageUrl = requireNotNull(dto.imageUrl)

        return ImageWidget(
            id = id,
            description = description,
            imageUrl = imageUrl,
        )
    }
}