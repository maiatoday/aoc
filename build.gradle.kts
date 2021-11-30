plugins {
    kotlin("jvm") version "1.6.0"
}

repositories {
    mavenCentral()
}
dependencies {
    implementation("junit:junit:4.13.1")
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "7.3"
    }
}
