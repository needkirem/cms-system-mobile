package dev.needkirem.cms.system.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class CmsWidget(
    val widgetType: String,
)