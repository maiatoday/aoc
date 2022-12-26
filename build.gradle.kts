//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0-RC2"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.4"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("junit:junit:4.13.2")
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.6")
}

benchmark {
    configurations {
        named("main") {
            iterationTime = 5
            iterationTimeUnit = "sec"
        }
    }
    targets {
        register("main") {
            this as kotlinx.benchmark.gradle.JvmBenchmarkTarget
            jmhVersion = "1.21"
        }
    }
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "7.6"
    }
}
//val compileKotlin: KotlinCompile by tasks
//compileKotlin.kotlinOptions {
//    languageVersion = "1.8"
//}