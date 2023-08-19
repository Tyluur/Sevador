plugins {
    application
    kotlin("jvm") version "1.9.0"
}

val koinVersion = "2.1.5"
val junitVersion = "5.6.2"
val jacksonVersion = "2.12.2"

allprojects {
    apply(plugin = "kotlin")
    apply(plugin = "idea")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    version = "0.0.1"

    java.sourceCompatibility = JavaVersion.toVersion('8')
    java.targetCompatibility = JavaVersion.toVersion('8')

    repositories {
        mavenCentral()
        mavenLocal()
        jcenter()
        maven(url = "https://repo.maven.apache.org/maven2")
        maven(url = "https://jitpack.io")
        maven(url = "https://dl.bintray.com/michaelbull/maven")
    }

}

application {
    mainClass.set("org.redrune.Bootstrap")
}

dependencies {
    // Java
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(kotlin("gradle-plugin", version = "1.5.0"))

    // Kotlin
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.4.2")

    // Dependency Injection
    implementation("org.koin", "koin-core", koinVersion)
    implementation("org.koin", "koin-logger-slf4j", koinVersion)

    // Reflection
    implementation("io.github.classgraph", "classgraph", "4.8.78")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.2.9") {
        exclude("org.slf4j", "slf4j-jdk14")
    }
    implementation("com.michael-bull.kotlin-inline-logger", "kotlin-inline-logger-jvm", "1.0.2")

    // RuneScape API
    implementation("com.displee", "rs-cache-library", "6.7")
    implementation("com.github.michaelbull", "rs-api", "1.1.1")

    // Utilities
    implementation("com.google.guava:guava:30.0-android")
    implementation("org.apache.commons:commons-lang3:3.10")
    implementation("commons-cli", "commons-cli", "1.4")
    implementation("it.unimi.dsi:fastutil:8.3.1")
    implementation("org.yaml", "snakeyaml", "1.26")

    // File IO
    implementation("com.fasterxml.jackson.core", "jackson-core", jacksonVersion)
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", jacksonVersion)
    implementation("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml", jacksonVersion)
    implementation("com.fasterxml.jackson.module", "jackson-module-paranamer", jacksonVersion)
    implementation("com.fasterxml.jackson.module", "jackson-module-parameter-names", jacksonVersion)
    implementation("com.fasterxml.jackson.datatype", "jackson-datatype-jdk8", jacksonVersion)
    implementation("com.fasterxml.jackson.datatype", "jackson-datatype-jsr310", jacksonVersion)

    // Database Management
    implementation("org.postgresql:postgresql:42.2.27")
    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("com.zaxxer", "HikariCP", "2.3.2")

    // Network
    implementation("io.netty:netty-all:4.1.86.Final")

    // Swing
    implementation("com.sun.activation:javax.activation:1.2.0")

    //Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testImplementation(group = "org.koin", name = "koin-test", version = koinVersion)
    testImplementation(group = "io.mockk", name = "mockk", version = "1.10.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

/*
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        useIR = true
    }
}*/
