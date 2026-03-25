plugins {
    java
}

version = rootProject.version

dependencies {
    compileOnly("com.yuvimasory:orange-extensions:1.3.0")
    implementation(project(":api"))
    runtimeOnly(project(":services"))

    // FlatLaf Look and Feel
    implementation("com.formdev:flatlaf:3.5.4")
}
