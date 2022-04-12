package dev.needkirem.cms.system.processor

import com.squareup.kotlinpoet.*
import dev.needkirem.cms.system.annotation.CmsWidgetMapper
import dev.needkirem.cms.system.mapper.JsonWidgetMapper
import dev.needkirem.cms.system.mapper.JsonWidgetMapperProvider
import dev.needkirem.cms.system.mapper.WidgetMapper
import dev.needkirem.cms.system.mapper.WidgetMapperProvider
import dev.needkirem.cms.system.utils.*
import kotlinx.serialization.json.JsonObject
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

class WidgetMappersGenerator {

    private var classPackage = ""

    fun generate(
        environment: ProcessingEnvironment,
        widgetTypeElements: List<TypeElement>,
    ) {
        val jsonMapperPairs = mutableListOf<Pair<String, String>>()
        val modelMapperTriples = mutableListOf<Triple<String, String, String>>()

        for (element in widgetTypeElements) {
            val widgetMapperAnnotation = element.getAnnotationMirror(CmsWidgetMapper::class.java)
            val dtoElement = widgetMapperAnnotation?.getAnnotationValueElement(environment, DTO_KEY)

            requireNotNull(dtoElement)

            val dtoClassName = dtoElement.getClassNameString()
            val widgetType = widgetMapperAnnotation.getAnnotationValueAsString(WIDGET_TYPE_KEY)

            val jsonMapperClassName = "$dtoClassName$JSON_MAPPER"
            val classPackage = "${element.getPackage()}$PACKAGE_POSTFIX"
            this.classPackage = classPackage

            val widgetStatement =
                "val widget = json.decodeFromJsonElement($dtoClassName.serializer(), $JSON_OBJECT_PARAM_NAME)"

            val mapFunction = FunSpec.builder(MAP_FUNCTION_NAME)
                .addModifiers(KModifier.OVERRIDE)
                .addParameter(JSON_OBJECT_PARAM_NAME, JsonObject::class)
                .addStatement(widgetStatement)
                .addStatement(RETURN_STATEMENT)
                .returns(dtoElement.getTypeName())
                .build()

            val mapperClass = TypeSpec.classBuilder(jsonMapperClassName)
                .superclass(JsonWidgetMapper::class)
                .addFunction(mapFunction)
                .build()

            val fileSpec = FileSpec.builder(classPackage, jsonMapperClassName)
                .addType(mapperClass)
                .build()

            jsonMapperPairs.add(widgetType to jsonMapperClassName)
            modelMapperTriples.add(
                Triple(
                    widgetType,
                    element.getClassName(),
                    element.getPackage().toString()
                )
            )

            fileSpec.writeTo(environment.filer)
        }

        generateJsonWidgetMapperProvider(
            environment = environment,
            mapperPairs = jsonMapperPairs,
        )
        generateModelWidgetMapperProvider(
            environment = environment,
            mapperTriples = modelMapperTriples,
        )
    }

    private fun generateJsonWidgetMapperProvider(
        environment: ProcessingEnvironment,
        mapperPairs: List<Pair<String, String>>,
    ) {
        val mapFunReturnType = typeNameOf<JsonWidgetMapper?>()
        val providerProperties = mutableListOf<PropertySpec>()
        var providerStatement = "return when($WIDGET_MAPPER_PROVIDER_GET_FUNCTION_PARAM) {\n"

        mapperPairs.forEach { (widget, mapper) ->
            val lazyProviderProperty = PropertySpec.builder(mapper, mapFunReturnType)
                .addModifiers(KModifier.PRIVATE)
                .delegate("lazy { $mapper() }")
                .build()
            providerProperties.add(lazyProviderProperty)
            providerStatement = "$providerStatement\"$widget\" -> $mapper\n"
        }
        providerStatement = "$providerStatement else -> null \n}"

        val mapperProviderGetFunction = FunSpec.builder(WIDGET_MAPPER_PROVIDER_GET_FUNCTION_NAME)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(WIDGET_MAPPER_PROVIDER_GET_FUNCTION_PARAM, typeNameOf<String?>())
            .addStatement(providerStatement)
            .returns(mapFunReturnType)
            .build()

        val mapperProviderClass = TypeSpec.classBuilder(JSON_MAPPER_PROVIDER_CLASS_NAME)
            .superclass(JsonWidgetMapperProvider::class)
            .addProperties(providerProperties)
            .addFunction(mapperProviderGetFunction)
            .build()

        val fileSpec = FileSpec.builder(classPackage, JSON_MAPPER_PROVIDER_CLASS_NAME)
            .addType(mapperProviderClass)
            .build()

        fileSpec.writeTo(environment.filer)
    }

    private fun generateModelWidgetMapperProvider(
        environment: ProcessingEnvironment,
        mapperTriples: List<Triple<String, String, String>>,
    ) {
        val mapFunReturnType = typeNameOf<WidgetMapper>()
        val providerProperties = mutableListOf<PropertySpec>()
        val imports = mutableListOf<Pair<String, String>>()
        var providerStatement = "return when($WIDGET_MAPPER_PROVIDER_GET_FUNCTION_PARAM) {\n"

        mapperTriples.forEach { (widget, mapper, pack) ->
            val lazyProviderProperty = PropertySpec.builder(mapper, mapFunReturnType)
                .addModifiers(KModifier.PRIVATE)
                .delegate("lazy { $mapper() }")
                .build()
            providerProperties.add(lazyProviderProperty)
            imports.add(pack to mapper)
            providerStatement = "$providerStatement\"$widget\" -> $mapper\n"
        }
        val throwStatement =
            "throw IllegalArgumentException(\"Cannot find widget mapper for widgetType = \$widgetType\")"
        providerStatement = "$providerStatement else -> $throwStatement \n}"

        val mapperProviderGetFunction = FunSpec.builder(WIDGET_MAPPER_PROVIDER_GET_FUNCTION_NAME)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(WIDGET_MAPPER_PROVIDER_GET_FUNCTION_PARAM, typeNameOf<String>())
            .addStatement(providerStatement)
            .returns(mapFunReturnType)
            .build()

        val mapperProviderClass = TypeSpec.classBuilder(MODEL_MAPPER_PROVIDER_CLASS_NAME)
            .superclass(WidgetMapperProvider::class)
            .addProperties(providerProperties)
            .addFunction(mapperProviderGetFunction)
            .build()

        val fileSpec = FileSpec.builder(classPackage, MODEL_MAPPER_PROVIDER_CLASS_NAME)
            .addType(mapperProviderClass)

        imports.forEach { (pack, name) ->
            fileSpec.addImport(pack, name)
        }

        fileSpec.build().writeTo(environment.filer)
    }

    companion object {
        private const val PACKAGE_POSTFIX = ".mappers"
        private const val JSON_MAPPER = "JsonMapper"
        private const val MAP_FUNCTION_NAME = "map"
        private const val JSON_OBJECT_PARAM_NAME = "jsonObject"
        private const val RETURN_STATEMENT = "return widget"
        private const val DTO_KEY = "dto"
        private const val WIDGET_TYPE_KEY = "widgetType"

        private const val JSON_MAPPER_PROVIDER_CLASS_NAME = "JsonWidgetMapperProviderImpl"
        private const val MODEL_MAPPER_PROVIDER_CLASS_NAME = "WidgetMapperProviderImpl"
        private const val WIDGET_MAPPER_PROVIDER_GET_FUNCTION_NAME = "get"
        private const val WIDGET_MAPPER_PROVIDER_GET_FUNCTION_PARAM = "widgetType"
    }
}
