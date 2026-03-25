import org.gradle.plugins.ide.eclipse.model.SourceFolder
import org.gradle.api.plugins.antlr.AntlrTask

plugins {
    java
    antlr
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<AntlrTask>().configureEach {
    arguments.addAll(listOf("-package", "org.jd.gui.util.parser.antlr"))
}

dependencies {
    implementation("com.fifesoft:rsyntaxtextarea:3.4.0")
    implementation("org.ow2.asm:asm:9.7")
    implementation("io.github.nbauma109:jd-core:1.3.3")
    implementation(project(":api"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    antlr("org.antlr:antlr4:4.13.1")
    implementation("org.antlr:antlr4-runtime:4.13.1")
}

version = rootProject.version

tasks.compileJava {
    dependsOn("generateGrammarSource")
    source("build/generated-src/antlr/main")
}

tasks.clean {
    delete("build/generated-src/antlr")
}

idea {
    module {
        sourceDirs.add(file("build/generated-src/antlr/main"))
    }
}

// eclipse {
//     classpath {
//         file {
//             whenMerged {
//                 entries.add(SourceFolder("build/generated-src/antlr/main", null))
//             }
//         }
//     }
// }