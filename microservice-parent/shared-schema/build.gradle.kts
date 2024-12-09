plugins {
    id("java-library")
    id("com.github.davidmc24.gradle.plugin.avro") version "1.5.0"
}

group = "ca.gbc"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.avro:avro:1.12.0")
}

avro {
    isCreateSetters = true
    fieldVisibility = "PRIVATE"
    isEnableDecimalLogicalType = true
}