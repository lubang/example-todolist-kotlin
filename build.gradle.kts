import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.11"
}

group = "com.github.lubang"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("joda-time:joda-time:2.10.1")
    implementation("ch.qos.logback:logback-classic:1.2.1")
    testImplementation(kotlin("test-junit"))
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")

    implementation("com.google.inject:guice:4.2.2")

    implementation("org.jetbrains.exposed:exposed:0.11.2")
    implementation("com.zaxxer:HikariCP:3.1.0")
    implementation("com.h2database:h2:1.4.197")

    val ktorVersion = "1.1.1"
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")
    implementation("io.ktor:ktor-gson:$ktorVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}