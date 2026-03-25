plugins {
    java
    distribution
    // id("nebula.ospackage") version "11.4.0"
    // id("edu.sc.seis.launch4j") version "4.0.0"
}

group = "org.jd"
version = "1.6.6"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.addAll(listOf("-Xlint:deprecation", "-Xlint:unchecked"))
    options.encoding = "UTF-8"
}

repositories {
    mavenCentral()
}

tasks.register<Delete>("cleanIdea") {
    delete("${project.name}.iws")
    delete("out")
}

subprojects {
    apply(plugin = "eclipse")
    apply(plugin = "idea")

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.addAll(listOf("-Xlint:deprecation", "-Xlint:unchecked"))
        options.encoding = "UTF-8"
    }
}