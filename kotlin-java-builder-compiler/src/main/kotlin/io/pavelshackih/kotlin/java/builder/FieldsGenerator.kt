package io.pavelshackih.kotlin.java.builder

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ClassName.Companion.bestGuess
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import javax.lang.model.element.Element

object FieldsGenerator {

    internal fun generateField(element: Element): PropertySpec {
        val fieldName = element.simpleName.toString()
        val type = element.correctTypeName()

        val propertySpec = PropertySpec.builder(fieldName,
                type,
                KModifier.PRIVATE)
                .mutable(true)

        val primitive = PRIMITIVE_WRAPPERS.findLast { it.className == type }
        primitive?.let {
            propertySpec.initializer(it.defValue)
        } ?: with(type) {
            if (nullable) {
                propertySpec.initializer("null")
            } else {
                propertySpec.addModifiers(KModifier.LATEINIT)
            }
        }

        return propertySpec.build()
    }

    private val PRIMITIVE_WRAPPERS = listOf(
            PrimitiveClassWrapper(bestGuess("kotlin.Boolean"), "false"),
            PrimitiveClassWrapper(bestGuess("kotlin.Byte"), "0b"),
            PrimitiveClassWrapper(bestGuess("kotlin.Short"), "0"),
            PrimitiveClassWrapper(bestGuess("kotlin.Int"), "0"),
            PrimitiveClassWrapper(bestGuess("kotlin.Long"), "0L"),
            PrimitiveClassWrapper(bestGuess("kotlin.Char"), "0"),
            PrimitiveClassWrapper(bestGuess("kotlin.Float"), "0f"),
            PrimitiveClassWrapper(bestGuess("kotlin.Double"), "0d")
    )

    private data class PrimitiveClassWrapper(val className: ClassName, val defValue: String)
}