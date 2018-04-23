package io.pavelshackih.kotlin.java.builder

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import org.jetbrains.annotations.Nullable
import javax.lang.model.element.Element

internal fun Element.asTypeName(): TypeName {
    val annotation = this.getAnnotation(Nullable::class.java)
    val typeName = this.asType().asTypeName()
    return if (annotation != null) typeName.asNullable() else typeName
}

internal fun Element.correctTypeName(): TypeName {
    val typeName = asTypeName()
    return if (isJavaString())
        when {
            typeName.nullable -> AnnotationProcessor.STRING_NULLABLE_CLASS_NAME
            else -> AnnotationProcessor.STRING_CLASS_NAME
        }
    else
        this.asTypeName()
}

internal fun Element.isJavaString() = this.asType().toString() == String::class.java.canonicalName
