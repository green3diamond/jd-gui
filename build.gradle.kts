plugins {
    java
    distribution
    id("org.owasp.dependencycheck") version "12.1.0"
}

dependencyCheck {
    formats = listOf("HTML", "JSON")
    failBuildOnCVSS = 7.0f  // Fail on HIGH and CRITICAL
}

val jdGuiVersion = "1.6.6"
val jdCoreVersion = "1.1.3"

version = jdGuiVersion
group = "org.jd"

allprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:deprecation", "-Xlint:unchecked"))
    }
}

// All in one JAR file
subprojects.forEach { evaluationDependsOn(it.path) }

tasks.jar {
    dependsOn(subprojects.map { it.tasks.named("jar") })

    val tmpSpiDir = file("build/tmp/spi")
    from(tmpSpiDir)

    val deps = mutableSetOf<File>()
    subprojects.forEach { subproject ->
        from(subproject.sourceSets["main"].output.classesDirs)
        from(subproject.sourceSets["main"].output.resourcesDir)
        // Use runtimeClasspath which already excludes compileOnly deps
        deps += subproject.configurations["runtimeClasspath"].resolve()
    }
    subprojects.forEach { subproject ->
        deps -= setOf(subproject.tasks.named<Jar>("jar").get().archiveFile.get().asFile)
    }
    from(deps.filter { it.isFile }.map { zipTree(it) })

    manifest {
        attributes(
            "Main-Class" to "org.jd.gui.App",
            "SplashScreen-Image" to "org/jd/gui/images/jd_icon_128.png",
            "JD-GUI-Version" to jdGuiVersion,
            "JD-Core-Version" to jdCoreVersion
        )
    }

    exclude(
        "META-INF/licenses/**", "META-INF/maven/**", "META-INF/INDEX.LIST",
        "**/ErrorStrip_*.properties", "**/RSyntaxTextArea_*.properties", "**/RTextArea_*.properties",
        "**/FocusableTip_*.properties", "**/RSyntaxTextArea_License.txt"
    )

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    doFirst {
        tmpSpiDir.deleteRecursively()
        val tmpSpiServicesDir = file("${tmpSpiDir.path}/META-INF/services")
        tmpSpiServicesDir.mkdirs()

        subprojects.forEach { subproject ->
            val servicesDir = file("${subproject.sourceSets["main"].output.resourcesDir}/META-INF/services")
            if (servicesDir.exists()) {
                servicesDir.listFiles()?.forEach { serviceFile ->
                    val target = file("${tmpSpiServicesDir.path}/${serviceFile.name}")
                    target.appendText(serviceFile.readText())
                }
            }
        }
    }
}

// Distribution - simplified for modern builds
distributions {
    main {
        contents {
            from(tasks.jar)
            from("LICENSE", "NOTICE", "README.md")
        }
    }
}
