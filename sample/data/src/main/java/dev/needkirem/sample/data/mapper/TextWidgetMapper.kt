package dev.needkirem.sample.data.mapper

import dev.needkirem.cms.system.annotation.CmsWidgetMapper
import dev.needkirem.cms.system.mapper.WidgetMapper
import dev.needkirem.domain.TextWidget
import dev.needkirem.sample.data.dto.TextWidgetDto

@CmsWidgetMapper(
    widgetType = "TextWidget",
    dto = TextWidgetDto::class,
    model = TextWidget::class,
)
class TextWidgetMapper : WidgetMapper<TextWidgetDto, TextWidget>() {

    override fun map(dto: TextWidgetDto): TextWidget {
        val id = requireNotNull(dto.id)
        val text = requireNotNull(dto.text)

        return TextWidget(
            id = id,
            text = text,
        )
    }
}