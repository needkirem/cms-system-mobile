package dev.needkirem.cms.system.utils

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.*
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass

val DECLARED_ELEMENT_KINDS = setOf(
    ElementKind.CLASS,
    ElementKind.INTERFACE,
    ElementKind.ENUM,
    ElementKind.ANNOTATION_TYPE
)

fun Element.asTypeElement(): TypeElement {
    if (!DECLARED_ELEMENT_KINDS.contains(kind)) {
        error("Cannot cast element $this to TypeElement, because in fact it is of kind $kind!")
    }
    return this as TypeElement
}

fun Element.getPackage(): PackageElement {
    var element = this
    while (element.kind != ElementKind.PACKAGE) {
        element = element.enclosingElement
    }
    return element as PackageElement
}

fun Element.getClassNameString(): String {
    return simpleName.toString()
}

fun Element.getTypeName(): TypeName {
    return asTypeElement().getTypeName()
}

fun TypeElement.equalTo(type: KClass<*>): Boolean {
    return this.qualifiedName.contentEquals(type.java.name)
}

fun Collection<TypeElement>.containsType(type: KClass<*>): Boolean {
    return this.any { element -> element.equalTo(type) }
}

fun TypeElement.getClassName(): String {
    return simpleName.toString()
}

fun TypeElement.getTypeName(): TypeName {
    return asType().asTypeName()
}

fun TypeElement.getAnnotationMirror(annotationClass: Class<*>): AnnotationMirror? {
    for (annotationMirror in annotationMirrors) {
        if (annotationMirror.annotationType.toString() == annotationClass.name) {
            return annotationMirror
        }
    }
    return null
}

fun AnnotationMirror.getAnnotationValueAsString(key: String): String {
    return getAnnotationValue(key)?.value as String
}

fun AnnotationMirror.getAnnotationValue(key: String): AnnotationValue? {
    for (elementValue in elementValues.entries) {
        if (elementValue.key.simpleName.toString() == key) {
            return elementValue.value
        }
    }
    return null
}

fun AnnotationMirror.getAnnotationValueElement(
    environment: ProcessingEnvironment,
    key: String,
): Element? {
    val value = getAnnotationValue(key)?.value
    return if (value != null) {
        environment.typeUtils.asElement(value as TypeMirror)
    } else {
        null
    }
}