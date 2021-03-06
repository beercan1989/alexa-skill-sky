import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String by project
val arrowVersion: String by project

plugins {
    idea
    application
    kotlin("jvm") version "1.3.71"
    kotlin("kapt") version "1.3.72"
    kotlin("plugin.serialization") version "1.3.71"
}

repositories {
    mavenCentral()
    maven("https://kotlin.bintray.com/ktor")
    maven("https://kotlin.bintray.com/kotlinx")
}

dependencies {
    // Use the Kotlin JDK 8 standard library
    implementation(kotlin("stdlib-jdk8"))

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.3.5")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.2.3")

    // Ktor server layer
    implementation("io.ktor:ktor-server-netty:$ktorVersion")

    // Ktor content negotiation
    implementation("io.ktor:ktor-serialization:$ktorVersion")

    // Ktor typed routes
    implementation("io.ktor:ktor-locations:$ktorVersion")

    // Ktor HTTP client
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization-jvm:$ktorVersion")

    // IP Address Helper
    implementation("com.github.maltalex:ineter:0.2.0")

    // Arrow KT
    implementation("io.arrow-kt:arrow-fx:$arrowVersion")
    implementation("io.arrow-kt:arrow-syntax:$arrowVersion")
    kapt("io.arrow-kt:arrow-meta:$arrowVersion")

    // Test framework
    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.0.2")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.0.2")

    // Mocking
    testImplementation("io.mockk:mockk:1.9.3")

    // Ktor server test kit
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")

    // Ktor client test kit
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock-jvm:$ktorVersion")
}

application {
    mainClassName = "uk.co.baconi.alexa.skill.sky.ApplicationKt"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = TestExceptionFormat.FULL
    }
}
