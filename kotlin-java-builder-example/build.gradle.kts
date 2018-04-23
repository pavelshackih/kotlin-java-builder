plugins {
    application
    kotlin("jvm") version "1.2.40"
    id("org.jetbrains.kotlin.kapt") version "1.2.40"
}

application {
    mainClassName = "io.pavelshackih.kotlin.java.builder.Main"
}

dependencies {
    compile(kotlin("stdlib"))
    compile(project(":kotlin-java-builder-annotations"))
    kapt(project(":kotlin-java-builder-compiler"))
}