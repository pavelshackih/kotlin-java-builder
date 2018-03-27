package io.pavelshackih.kotlin.java.builder

import com.squareup.kotlinpoet.*
import org.jetbrains.annotations.Nullable
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("io.pavelshackih.kotlin.java.builder.SimpleJavaBuilder")
@SupportedOptions(AnnotationProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class AnnotationProcessor : AbstractProcessor() {

    private lateinit var elements: Elements
    private lateinit var types: Types
    private lateinit var messager: Messager

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
        val STRING_CLASS_NAME = ClassName("kotlin", "String")
        val STRING_NULLABLE_CLASS_NAME = STRING_CLASS_NAME.asNullable()
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        elements = processingEnv.elementUtils
        messager = processingEnv.messager
        types = processingEnv.typeUtils
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        val elements = roundEnv.getElementsAnnotatedWith(SimpleJavaBuilder::class.java)

        if (elements.isEmpty()) {
            return false
        }

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: run {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Can't find the target directory for generated Kotlin files.")
            return false
        }

        elements.forEach { element ->
            File(kaptKotlinGeneratedDir, generateFileName(element)).apply {
                parentFile.mkdirs()
                processElement(element).writeTo(this)
            }
        }

        return false
    }

    private fun processElement(element: Element): FileSpec {
        if (element is TypeElement) {
            val packageName = elements.getPackageOf(element).qualifiedName.toString()
            val className = ClassName(packageName, "${element.simpleName}Builder")
            val typeSpec = TypeSpec.classBuilder(className)
            val fileSpec = FileSpec.builder(packageName, generateFileName(element))

            val fields = element.enclosedElements.filter { it.kind == ElementKind.FIELD }

            fields.forEach {
                typeSpec.addProperty(generateField(it))
            }

            fields.forEach {
                typeSpec.addFunction(generateSetter(it))
            }

            typeSpec.addFunction(generateBuildFun(element, fields))

            fileSpec.addType(typeSpec.build())
            return fileSpec.build()
        }
        throw IllegalStateException("Wrong element type $element")
    }

    private fun generateField(element: Element): PropertySpec {
        val fieldName = element.simpleName.toString()
        val type = element.correctTypeName()
        type.
        val propertySpec = PropertySpec.builder(fieldName,
                type,
                KModifier.PRIVATE, KModifier.LATEINIT)
                .mutable(true)
        return propertySpec.build()
    }

    private fun Element.correctTypeName(): TypeName {
        val typeName = this.asTypeName()
        return if (this.isJavaString())
            when {
                typeName.nullable -> STRING_NULLABLE_CLASS_NAME
                else -> STRING_CLASS_NAME
            }
        else
            typeName
    }

    private val PRIMITIVE_TYPES = listOf(BOOLEAN, BYTE, CHAR, DOUBLE, FLOAT, INT, LONG, SHORT)

    private fun Element.isJavaString() = this.asType().toString() == String::class.java.canonicalName

    private fun generateSetter(element: Element): FunSpec {
        val field = element.simpleName.toString()
        return FunSpec.builder(field)
                .addParameter(field, element.correctTypeName())
                .addCode("return apply { this.%N = %N }", field, field)
                .build()
    }

    private fun generateBuildFun(typeElement: TypeElement, elements: List<Element>): FunSpec {
        val funSpec = FunSpec.builder("build")
        val codeBlock = CodeBlock.builder()
        codeBlock.add("return %T(", typeElement.asClassName())
        elements.forEachIndexed { i, field ->
            codeBlock.add("%N = %N!!", field.simpleName, field.simpleName)
            if (i != elements.size - 1) {
                codeBlock.add(", ")
            }
        }
        codeBlock.add(")")
        funSpec.addCode(codeBlock.build())
        return funSpec.build()
    }

    private fun generateFileName(element: Element): String = "${element.simpleName}Builder"

    private fun Element.asTypeName(): TypeName {
        val annotation = this.getAnnotation(Nullable::class.java)
        val typeName = this.asType().asTypeName()
        return if (annotation != null) typeName.asNullable() else typeName
    }
}