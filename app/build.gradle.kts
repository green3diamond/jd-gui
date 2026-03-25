plugins {
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    compileOnly(project(":api"))
    runtimeOnly(project(":services"))
    compileOnly("com.yuvimasory:orange-extensions:1.3.0")
    implementation("com.formdev:flatlaf:3.4")
    implementation("com.miglayout:miglayout-swing:11.3")
}

version = rootProject.version