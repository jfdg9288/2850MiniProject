plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.serialization") version "1.9.25"
    application
}

group = "com.library"
version = "1.0.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    // Ktor server
    implementation("io.ktor:ktor-server-core-jvm:2.3.12")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.12")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.12")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.12")

    // Database + H2
    implementation("org.jetbrains.exposed:exposed-core:0.45.0")
	implementation("org.jetbrains.exposed:exposed-dao:0.45.0")
	implementation("org.jetbrains.exposed:exposed-jdbc:0.45.0")
    implementation("com.h2database:h2:2.3.232")

    // CSV parsing (handles commas/quotes properly)
    implementation("com.opencsv:opencsv:5.9")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.5.6")
}

application {
    mainClass.set("com.library.library.ApplicationKt")
}

kotlin {
    jvmToolchain(21)
}