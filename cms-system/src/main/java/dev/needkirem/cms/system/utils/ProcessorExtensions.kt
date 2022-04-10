package dev.needkirem.cms.system.utils

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

val DECLARED_ELEMENT_KINDS = setOf(
    ElementKind.CLASS,
    ElementKind.INTERFACE,
    ElementKind.ENUM,
    ElementKind.ANNOTATION_TYPE
)

fun TypeElement.equalTo(type: KClass<*>): Boolean {
    return this.qualifiedName.contentEquals(type.java.name)
}

fun Collection<TypeElement>.containsType(type: KClass<*>): Boolean {
    return this.any { element -> element.equalTo(type) }
}

fun Element.asTypeElement(): TypeElement {
    if (!DECLARED_ELEMENT_KINDS.contains(kind)) {
        error("Cannot cast element $this to TypeElement, because in fact it is of kind $kind!")
    }
    return this as TypeElement
}

fun TypeElement.getClassName(): String {
    return simpleName.toString()
}

fun Element.getPackage(): PackageElement {
    var element = this
    while (element.kind != ElementKind.PACKAGE) {
        element = element.enclosingElement
    }
    return element as PackageElement
}

fun TypeElement.getTypeName(): TypeName {
    return asType().asTypeName()
}