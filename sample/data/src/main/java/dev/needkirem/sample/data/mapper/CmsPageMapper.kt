package dev.needkirem.sample.data.mapper

import dev.needkirem.cms.system.mapper.CmsSystemWidgetMapper
import dev.needkirem.domain.CmsPage
import dev.needkirem.sample.data.dto.CmsPageDto
import dev.needkirem.sample.data.mapper.mappers.JsonWidgetMapperProviderImpl
import dev.needkirem.sample.data.mapper.mappers.WidgetMapperProviderImpl

class CmsPageMapper {

    private val widgetMapper = CmsSystemWidgetMapper(
        jsonWidgetMapperProvider = JsonWidgetMapperProviderImpl(),
        modelWidgetMapperProvider = WidgetMapperProviderImpl(),
    )

    fun map(cmsPageDto: CmsPageDto): CmsPage {
        val version = cmsPageDto.version.orEmpty()
        val widgets = cmsPageDto.widgets?.let {
            widgetMapper.map(it)
        }.orEmpty()

        return CmsPage(
            version = version,
            widgets = widgets,
        )
    }
}