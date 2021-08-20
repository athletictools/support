import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.serialization") version "1.5.10"
    application
}

group = "me.john"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val ktorVersion = "1.6.2"

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")

    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("com.auth0:java-jwt:3.18.1")
    implementation("io.ktor:ktor-serialization:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
    implementation("org.slf4j:slf4j-simple:1.7.25")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    sourceSets {
        main {
            kotlin.srcDir("src/main")
            resources.srcDir("src/resources")
        }
        test {
            kotlin.srcDir("src/test")
        }
    }
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("ServerKt")
}
