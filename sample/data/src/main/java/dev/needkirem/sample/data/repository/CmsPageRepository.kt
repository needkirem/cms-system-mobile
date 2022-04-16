package dev.needkirem.sample.data.repository

import dev.needkirem.domain.CmsPage
import dev.needkirem.sample.data.api.getJsonApi
import dev.needkirem.sample.data.mapper.CmsPageMapper

class CmsPageRepository {

    private val jsonApi = getJsonApi()

    private val cmsPageMapper = CmsPageMapper()

    suspend fun getCmsPage(): CmsPage {
        val dto = jsonApi.getCmsPage()
        return cmsPageMapper.map(dto)
    }
}