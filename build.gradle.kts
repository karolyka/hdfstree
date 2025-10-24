plugins {
    kotlin("jvm") version "2.2.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "org.chas"
version = "2.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("commons-cli:commons-cli:1.10.0")
    implementation("org.apache.hadoop:hadoop-client:3.4.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
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
