package dev.needkirem.cms.system.processor

import com.google.auto.service.AutoService
import dev.needkirem.cms.system.annotation.CmsWidgetMapper
import dev.needkirem.cms.system.utils.asTypeElement
import dev.needkirem.cms.system.utils.containsType
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.AGGREGATING)
@AutoService(Processor::class)
class WidgetMappersProcessor : AbstractProcessor() {

    private val widgetMappersGenerator = WidgetMappersGenerator()

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun getSupportedAnnotationTypes() = setOf(
        CMS_WIDGET_ANNOTATION.java.name
    )

    private fun isProcessingClaimed(annotations: MutableSet<out TypeElement>): Boolean {
        return annotations.containsType(CMS_WIDGET_ANNOTATION)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {
        if (annotations != null && roundEnvironment != null && isProcessingClaimed(annotations)) {
            return try {
                process(roundEnvironment)
            } catch (exception: Exception) {
                val message =
                    "An error occurred while generating mappers provider! $exception"
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    message
                )
                true
            }
        }
        return false
    }

    @Throws(Exception::class)
    private fun process(roundEnvironment: RoundEnvironment): Boolean {
        val annotatedElements =
            roundEnvironment.getElementsAnnotatedWith(CMS_WIDGET_ANNOTATION.java)
                .onEach { element -> checkIsApplicableForMapperProvider(element) }
                .map { element -> element.asTypeElement() }

        widgetMappersGenerator.generate(
            environment = processingEnv,
            widgetTypeElements = annotatedElements,
        )

        return true
    }

    private fun checkIsApplicableForMapperProvider(element: Element) {
        if (element.kind != ElementKind.CLASS) {
            throw IllegalArgumentException("Element $element is not applicable for $CMS_WIDGET_ANNOTATION annotation!")
        }
    }

    companion object {
        private val CMS_WIDGET_ANNOTATION = CmsWidgetMapper::class
    }
}