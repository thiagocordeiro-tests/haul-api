import org.gradle.api.tasks.testing.logging.TestExceptionFormat

val coroutines: String by project
val http4k: String by project
val mockk: String by project
val logback: String by project
val logstashVersion: String by project
val microutils: String by project
val jacksonVersion: String by project
val ormVersion: String by project
val ormMigrationVersion: String by project
val mysqlConnectorVersion: String by project

plugins {
    kotlin("jvm") version "2.0.0"
    application
    jacoco
}

application {
    mainClass.set("ApplicationKt")
}

group = "com.haulcompliance.inspections"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(platform("org.http4k:http4k-bom:$http4k"))
    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-server-netty")
    implementation("org.http4k:http4k-multipart")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")

    implementation("io.github.microutils:kotlin-logging-jvm:$microutils")
    implementation("ch.qos.logback:logback-core:$logback")
    implementation("ch.qos.logback:logback-classic:$logback")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashVersion")

    implementation("io.tcds.orm:orm:$ormVersion")
    implementation("io.tcds.orm:migrations:$ormMigrationVersion")
    implementation("com.mysql:mysql-connector-j:$mysqlConnectorVersion")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:$mockk")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")
    testImplementation("io.tcds.orm:testing:0.1.2")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("started", "skipped", "passed", "failed")

        showCauses = true
        showStackTraces = true
        showExceptions = true
        exceptionFormat = TestExceptionFormat.FULL
    }
    finalizedBy(tasks.jacocoTestReport)
}

tasks.withType<Jar> { duplicatesStrategy = DuplicatesStrategy.EXCLUDE }
tasks.withType<Tar> { duplicatesStrategy = DuplicatesStrategy.EXCLUDE }
tasks.withType<Zip> { duplicatesStrategy = DuplicatesStrategy.EXCLUDE }

jacoco {
    toolVersion = "0.8.7"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
    }
}
