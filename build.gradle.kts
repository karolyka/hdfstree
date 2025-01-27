import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "org.chas"
version = "2.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("commons-cli:commons-cli:1.9.0")
    implementation("org.apache.hadoop:hadoop-client:3.4.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("com.trovit.hdfstree.HdfsTreeKt")
}

tasks.shadowJar {
    mustRunAfter(tasks.distZip, tasks.distTar)
    archiveBaseName.set(project.name)
    archiveClassifier.set("")
    archiveVersion.set(project.version.toString())
}
