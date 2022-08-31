val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.7.10"
    id("io.ktor.plugin") version "2.1.0"
    kotlin("plugin.serialization") version "1.7.10"
}

group = "com.aidandagnall"
version = "0.0.1"
application {
    mainClass.set("com.aidandagnall.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

tasks {
    create("stage").dependsOn("installDist")
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.litote.kmongo:kmongo:4.7.0")
    implementation("org.litote.kmongo:kmongo-id:4.7.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.3.1")
    implementation("io.ktor:ktor-serialization-jackson:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("io.ktor.plugin:plugin:2.1.0")
    }
}
apply(plugin = "io.ktor.plugin")