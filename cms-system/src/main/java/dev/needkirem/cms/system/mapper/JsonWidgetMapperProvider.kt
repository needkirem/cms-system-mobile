package dev.needkirem.cms.system.mapper

abstract class JsonWidgetMapperProvider {

    abstract fun get(widgetType: String?): JsonWidgetMapper?
}