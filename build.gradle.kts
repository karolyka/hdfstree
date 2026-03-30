plugins {
    kotlin("jvm") version "2.3.20"
    id("com.gradleup.shadow") version "9.4.1"
    application
}

group = "org.chas"
version = "2.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("commons-cli:commons-cli:1.11.0")
    implementation("org.apache.hadoop:hadoop-client:3.4.3")
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
