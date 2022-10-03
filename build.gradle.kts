val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project

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
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
//    implementation("org.litote.kmongo:kmongo:4.7.0")
//    implementation("org.litote.kmongo:kmongo-id:4.7.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.3.1")
    implementation("io.ktor:ktor-serialization-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("mysql:mysql-connector-java:8.0.30")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("com.auth0:auth0:1.43.0")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:2.13.4")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.4")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.13.4")
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