import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("application")
    kotlin("jvm") version "2.1.10"
}
repositories {
    mavenLocal()
    mavenCentral()
}

project.group = "jaxb.gradle"

val ktVersion: String = project.findProperty("kotlinVersion") as? String ?: "2.1.10"

val jaxb: Configuration by configurations.creating
val jaxbVersion: String = project.findProperty("jaxbVersion") as? String ?: "4.0.5"
val schemaDir = "${layout.projectDirectory}/schemas"
val xjcOutputDir = "${layout.buildDirectory.get()}/generated/sources/xjc/main"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")
    testRuntimeOnly("org.glassfish.jaxb:jaxb-runtime:$jaxbVersion")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$ktVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.12.1")

    jaxb("org.glassfish.jaxb:jaxb-xjc:$jaxbVersion")
    jaxb("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")
    jaxb("org.glassfish.jaxb:jaxb-runtime:$jaxbVersion")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events("failed")
        exceptionFormat = TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }
}

tasks.wrapper {
    gradleVersion = "8.13"
}

val createXjcOutputDir by tasks.register("createXjcOutputDir") {
    doLast {
        mkdir(xjcOutputDir)
    }
}

val xjc by tasks.registering(JavaExec::class) {
    dependsOn(createXjcOutputDir)
    logger.info("Generating from *.xsd in $schemaDir to *.java in $xjcOutputDir")
    classpath = jaxb
    mainClass.set("com.sun.tools.xjc.XJCFacade")
    args = listOf(
        "-d", xjcOutputDir,
        "-p", "${project.group}.jaxb",
        "-b", "$schemaDir/bindings.xjb",
        "-encoding", "UTF-8",
        "-no-header",
        "-quiet",
        "-extension",
        schemaDir
    )
}

tasks.withType<JavaCompile>().configureEach {
    dependsOn(xjc)
}

sourceSets.main {
    java {
        srcDirs(files(xjcOutputDir) { builtBy(xjc) })
    }
}