package dev.needkirem.sample.data.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.needkirem.sample.data.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Converter
import retrofit2.Retrofit

private fun getConverterFactory(): Converter.Factory {
    val contentType = requireNotNull(MediaType.parse("application/json"))
    val json = Json {
        ignoreUnknownKeys = true
    }
    return json.asConverterFactory(contentType)
}

fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(getConverterFactory())
        .baseUrl(BuildConfig.jsonBaseUrl)
        .build()
}