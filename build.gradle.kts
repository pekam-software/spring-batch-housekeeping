val shedlockVersion = "5.12.0"
val springBootVersion = "3.2.3"
val jUnitVersion = "5.10.2"

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.freefair.lombok") version "8.6"
    id("maven-publish")
    id("java-library")
}

group = "org.pekam.springbatch"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-jdbc:$springBootVersion")

    implementation("net.javacrumbs.shedlock:shedlock-spring:$shedlockVersion")
    implementation("net.javacrumbs.shedlock:shedlock-provider-jdbc-template:$shedlockVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("org.springframework.batch:spring-batch-test:5.1.1")

    testImplementation("com.h2database:h2:2.2.224")

    testImplementation(platform("org.junit:junit-bom:$jUnitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter:$jUnitVersion")
}

tasks.test {
    useJUnitPlatform()
}