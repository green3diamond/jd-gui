plugins {
    java
    antlr
}

version = rootProject.version

dependencies {
    implementation("com.fifesoft:rsyntaxtextarea:3.5.3")
    implementation("org.ow2.asm:asm:9.7.1")
    implementation(files("${rootProject.projectDir}/libs/jd-core-1.1.3.jar"))
    implementation("org.antlr:antlr4-runtime:4.13.2")
    implementation(project(":api"))

    antlr("org.antlr:antlr4:4.13.2")

    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.14.2")
    testImplementation("org.mockito:mockito-junit-jupiter:5.14.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.generateGrammarSource {
    arguments = arguments + listOf(
        "-package", "org.jd.gui.util.parser.antlr",
        "-visitor"
    )
    outputDirectory = file("${layout.buildDirectory.get()}/generated-src/antlr/main/org/jd/gui/util/parser/antlr")
}

// Exclude the default ANTLR-generated source directory to avoid duplicate classes
sourceSets {
    main {
        java {
            // Only include the package-specific directory
            srcDir("${layout.buildDirectory.get()}/generated-src/antlr/main")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
