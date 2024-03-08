val shedlockVersion = "5.12.0"
val springBootVersion = "3.2.3"
val jUnitVersion = "5.10.2"

plugins {
    id("org.springframework.boot") version "3.2.3" apply false
    id("org.shipkit.shipkit-auto-version") version "2.0.4"
    id("io.freefair.lombok") version "8.6"
    id("maven-publish")
    id("java-library")
    signing
}

publishing {
    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/pekam-software/spring-batch-housekeeping")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

group = "io.github.pekam-software"
version = "0.0.2-SNAPSHOT"

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
    testImplementation("org.flywaydb:flyway-core:10.9.0")

    testImplementation(platform("org.junit:junit-bom:$jUnitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter:$jUnitVersion")
}

java {
    withJavadocJar()
    withSourcesJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "spring-batch-housekeeping"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name = "Spring Batch Housekeeping"
                description = "A library for improving the performance of Spring Batch"
                url = "https://github.com/pekam-software/spring-batch-housekeeping"
                licenses {
                    license {
                        name = "GNU GENERAL PUBLIC LICENSE, Version 3.0"
                        url = "https://www.gnu.org/licenses/gpl-3.0.html"
                    }
                }
                developers {
                    developer {
                        id = "violence102"
                        name = "Przemysław Zajadlak"
                        email = "przemyslaw.zajadlak@gmail.com"
                    }
                }
                scm {
                    connection = "scm:git:git://pekam-software/spring-batch-housekeeping.git"
                    developerConnection = "scm:git:ssh://pekam-software/spring-batch-housekeeping.git"
                    url = "https://github.com/pekam-software/spring-batch-housekeeping"
                }
            }
        }
    }
    repositories {
        maven {
            val releasesRepoUrl = uri(layout.buildDirectory.dir("https://github.com/pekam-software/spring-batch-housekeeping/releases"))
            val snapshotsRepoUrl = uri(layout.buildDirectory.dir("https://github.com/pekam-software/spring-batch-housekeeping/snapshots"))
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

tasks.test {
    useJUnitPlatform()
}