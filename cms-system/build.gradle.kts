plugins {
    kotlin("kapt")
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.6.10"
    id("maven-publish")
}

group = "com.github.needkirem"
version = "0.1"

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.20")

    kapt("net.ltgt.gradle.incap:incap-processor:0.3")
    implementation("net.ltgt.gradle.incap:incap:0.3")

    kapt("com.google.auto.service:auto-service:1.0.1")
    implementation("com.google.auto.service:auto-service-annotations:1.0.1")

    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.20")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("com.squareup:kotlinpoet:1.11.0")
}