plugins {
    kotlin("jvm") version "1.3.41"
    idea
}

java {
    sourceCompatibility = JavaVersion.VERSION_12
    targetCompatibility = JavaVersion.VERSION_12
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.41"))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.pdfbox:pdfbox:2.0.16")
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test-junit"))
}
