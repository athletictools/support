import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"
    kotlin("plugin.serialization") version "1.5.30"
    application
}

group = "me.john"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val ktorVersion = "1.6.3"
val exposedVersion = "0.31.1"

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")

    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("com.auth0:java-jwt:3.18.1")
    implementation("io.ktor:ktor-serialization:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
    implementation("org.slf4j:slf4j-simple:1.7.32")
    implementation("org.kodein.di:kodein-di:7.7.0")
    implementation("org.litote.kmongo:kmongo-coroutine:4.2.8")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    sourceSets {
        main {
            kotlin.srcDir("src/main")
        }
        test {
            kotlin.srcDir("src/test")
        }
    }
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "16"
}

application {
    mainClass.set("support.ServerKt")
}
