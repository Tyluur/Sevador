plugins {
	application
	kotlin("jvm") version "1.9.0"
}

val junitVersion = "5.6.2"

apply(plugin = "idea")

version = "0.0.1"

java.sourceCompatibility = JavaVersion.toVersion('8')
java.targetCompatibility = JavaVersion.toVersion('8')

repositories {
	mavenCentral()
}

application {
	mainClass.set("com.sevador.Main")
}

dependencies {
	// Java
	implementation(kotlin("stdlib-jdk8"))
	implementation(kotlin("reflect"))
	implementation(kotlin("gradle-plugin", version = "1.5.0"))

	// Kotlin
	implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.4.2")

	// Reflection
	implementation("io.github.classgraph", "classgraph", "4.8.78")

	// Logging
	implementation("ch.qos.logback:logback-classic:1.2.9") {
		exclude("org.slf4j", "slf4j-jdk14")
	}

	implementation("com.thoughtworks.xstream:xstream:1.4.17")

	implementation("io.netty:netty:3.9.9.Final")
	implementation("org.jruby:jruby:9.3.2.0")

	//Testing
	testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
	testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
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