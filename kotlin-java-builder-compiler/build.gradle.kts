plugins {
    kotlin("jvm") version "1.2.40"
}

dependencies {
    compile(kotlin("stdlib"))
    compile(project(":kotlin-java-builder-annotations"))
    compile("com.squareup", "kotlinpoet", "0.7.0")
}