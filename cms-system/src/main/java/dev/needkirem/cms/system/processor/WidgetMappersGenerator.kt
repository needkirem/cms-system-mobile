package dev.needkirem.cms.system.processor

import com.squareup.kotlinpoet.*
import dev.needkirem.cms.system.annotation.CmsWidget
import dev.needkirem.cms.system.mapper.WidgetMapper
import dev.needkirem.cms.system.mapper.WidgetMapperProvider
import dev.needkirem.cms.system.models.CmsPage
import dev.needkirem.cms.system.utils.getClassName
import dev.needkirem.cms.system.utils.getPackage
import dev.needkirem.cms.system.utils.getTypeName
import kotlinx.serialization.json.JsonObject
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

class WidgetMappersGenerator {

    private var classPackage = ""
    private val providerWidgetGetMapperPairs = mutableListOf<Pair<String, String>>()

    fun generate(
        environment: ProcessingEnvironment,
        widgetTypeElements: List<TypeElement>,
    ) {
        for (widgetTypeElement in widgetTypeElements) {
            val widgetClassName = widgetTypeElement.getClassName()
            val widgetTypeName = widgetTypeElement.getTypeName()
            val widgetTypeAnnotation =
                widgetTypeElement.getAnnotation(CmsWidget::class.java).widgetType

            val mapperClassName = "$widgetClassName$MAPPER"
            val classPackage = "${widgetTypeElement.getPackage()}$PACKAGE_POSTFIX"
            this.classPackage = classPackage

            val widgetStatement =
                "val widget = json.decodeFromJsonElement($widgetClassName.serializer(), $JSON_OBJECT_PARAM_NAME)"

            val mapFunction = FunSpec.builder(MAP_FUNCTION_NAME)
                .addModifiers(KModifier.OVERRIDE)
                .addParameter(JSON_OBJECT_PARAM_NAME, JsonObject::class)
                .addStatement(widgetStatement)
                .addStatement(RETURN_STATEMENT)
                .returns(widgetTypeName)
                .build()

            val mapperClass = TypeSpec.classBuilder(mapperClassName)
                .superclass(WidgetMapper::class)
                .addFunction(mapFunction)
                .build()

            val fileSpec = FileSpec.builder(classPackage, mapperClassName)
                .addType(mapperClass)
                .build()

            providerWidgetGetMapperPairs.add(widgetTypeAnnotation to mapperClassName)

            fileSpec.writeTo(environment.filer)
        }

        generateWidgetMapperProvider(environment)
        generateCmsPageMapper(environment)
    }

    private fun generateWidgetMapperProvider(
        environment: ProcessingEnvironment,
    ) {
        val providerProperties = mutableListOf<PropertySpec>()
        var providerStatement = "return when($WIDGET_MAPPER_PROVIDER_GET_FUNCTION_PARAM) {\n"

        providerWidgetGetMapperPairs.forEach { (widget, mapper) ->
            val lazyProviderProperty = PropertySpec.builder(mapper, WidgetMapper::class)
                .addModifiers(KModifier.PRIVATE)
                .delegate("lazy { $mapper() }")
                .build()
            providerProperties.add(lazyProviderProperty)
            providerStatement = "$providerStatement\"$widget\" -> $mapper\n"
        }
        providerStatement = "$providerStatement else -> null \n}"

        val mapperProviderReturnType = typeNameOf<WidgetMapper?>()
        val mapperProviderGetFunction = FunSpec.builder(WIDGET_MAPPER_PROVIDER_GET_FUNCTION_NAME)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(WIDGET_MAPPER_PROVIDER_GET_FUNCTION_PARAM, typeNameOf<String?>())
            .addStatement(providerStatement)
            .returns(mapperProviderReturnType)
            .build()

        val mapperProviderClass = TypeSpec.classBuilder(WIDGET_MAPPER_PROVIDER_CLASS_NAME)
            .superclass(WidgetMapperProvider::class)
            .addProperties(providerProperties)
            .addFunction(mapperProviderGetFunction)
            .build()

        val fileSpec = FileSpec.builder(classPackage, WIDGET_MAPPER_PROVIDER_CLASS_NAME)
            .addType(mapperProviderClass)
            .build()

        fileSpec.writeTo(environment.filer)
    }

    private fun generateCmsPageMapper(
        environment: ProcessingEnvironment,
    ) {
        val widgetMapperProviderAbstractClassName =
            WidgetMapperProvider::class.simpleName.toString()
        val widgetMapperProviderProperty = PropertySpec.builder(
            widgetMapperProviderAbstractClassName,
            WidgetMapperProvider::class
        )
            .delegate("lazy { $WIDGET_MAPPER_PROVIDER_CLASS_NAME() }")
            .build()

        val widgetMapperErrorProperty = PropertySpec.builder(MAPPER_ERROR_TAG, String::class)
            .initializer(CMS_PAGE_MAPPER_ERROR_INITIALIZER)
            .build()

        val mapFunction = FunSpec.builder(MAP_FUNCTION_NAME)
            .returns(CmsPage::class)
            .addParameter(JSON_OBJECT_PARAM_NAME, JsonObject::class)
            .addStatement("val jsonWidgets = jsonObject.getArray(\"$CMS_PAGE_MAPPER_WIDGETS_KEY\")")
            .addStatement("val widgets = jsonWidgets.mapNotNull { jsonElement ->")
            .addStatement("val widgetType = jsonElement.jsonObject.getString(\"$CMS_PAGE_MAPPER_WIDGET_TYPE_KEY\")")
            .addStatement("val mapper = $widgetMapperProviderAbstractClassName.get(widgetType)")
            .addStatement("try { mapper?.map(jsonElement.jsonObject) }")
            .addStatement("catch (throwable: Throwable) { null } }")
            .addStatement("return CmsPage(widgets)")
            .build()

        val cmsMapperClass = TypeSpec.classBuilder(CMS_PAGE_MAPPER_CLASS_NAME)
            .addProperty(widgetMapperErrorProperty)
            .addProperty(widgetMapperProviderProperty)
            .addFunction(mapFunction)
            .build()

        val fileSpec = FileSpec.builder(classPackage, CMS_PAGE_MAPPER_CLASS_NAME)
            .addType(cmsMapperClass)
            .addImport("dev.needkirem.cms.system.utils", "getArray", "getString")
            .addImport("kotlinx.serialization.json", "jsonObject")
            .build()

        fileSpec.writeTo(environment.filer)
    }

    companion object {
        private const val PACKAGE_POSTFIX = ".mappers"
        private const val MAPPER = "Mapper"
        private const val MAP_FUNCTION_NAME = "map"
        private const val JSON_OBJECT_PARAM_NAME = "jsonObject"
        private const val RETURN_STATEMENT = "return widget"

        private const val WIDGET_MAPPER_PROVIDER_CLASS_NAME = "WidgetMapperProviderImpl"
        private const val WIDGET_MAPPER_PROVIDER_GET_FUNCTION_NAME = "get"
        private const val WIDGET_MAPPER_PROVIDER_GET_FUNCTION_PARAM = "widgetType"

        private const val CMS_PAGE_MAPPER_CLASS_NAME = "CmsPageMapper"
        private const val MAPPER_ERROR_TAG = "MAPPER_ERROR_TAG"
        private const val CMS_PAGE_MAPPER_ERROR_INITIALIZER = "\"CmsSystemMapperError\""
        private const val CMS_PAGE_MAPPER_WIDGETS_KEY = "widgets"
        private const val CMS_PAGE_MAPPER_WIDGET_TYPE_KEY = "widgetType"
    }
}
