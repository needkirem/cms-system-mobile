package dev.needkirem.cms.system.mapper

abstract class WidgetMapperProvider {

    abstract fun get(widgetType: String?): WidgetMapper?
}