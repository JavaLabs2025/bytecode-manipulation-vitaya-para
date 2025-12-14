plugins {
    id("java")
    id("application")
}

application {
    mainClass.set("org.example.Example")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
    implementation("org.ow2.asm:asm:9.5")
    implementation("org.ow2.asm:asm-tree:9.5")
    implementation("org.ow2.asm:asm-analysis:9.5")
    implementation("org.ow2.asm:asm-util:9.5")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
}