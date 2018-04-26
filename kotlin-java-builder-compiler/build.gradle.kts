import org.gradle.api.tasks.bundling.Jar

plugins {
    kotlin("jvm") version "1.2.40"
    `java-library`
    `maven-publish`
}

dependencies {
    compile(kotlin("stdlib"))
    compile(project(":kotlin-java-builder-annotations"))
    compile("com.squareup", "kotlinpoet", "0.7.0")
}