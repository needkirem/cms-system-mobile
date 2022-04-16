package dev.needkirem.sample.data.api

import dev.needkirem.sample.data.BuildConfig
import dev.needkirem.sample.data.dto.CmsPageDto
import retrofit2.http.GET

interface JsonApi {

    @GET(BuildConfig.jsonPath)
    suspend fun getCmsPage(): CmsPageDto
}