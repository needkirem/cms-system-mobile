package dev.needkirem.sample.data.api

fun getJsonApi(): JsonApi {
    return getRetrofit().create(JsonApi::class.java)
}