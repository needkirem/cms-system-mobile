package dev.needkirem.cms.system.annotation

import kotlin.reflect.KClass

@Target(allowedTargets = [AnnotationTarget.CLASS])
@Retention(AnnotationRetention.SOURCE)
annotation class CmsWidgetMapper(
    val widgetType: String,
    val dto: KClass<*>,
    val model: KClass<*>
)