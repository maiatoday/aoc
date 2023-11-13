plugins {
    kotlin("jvm") version "1.9.20"
    application
    id("org.jetbrains.kotlinx.benchmark") version "0.4.9"
}

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    testImplementation(kotlin("test"))

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

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("days/MainKt") // The main class of the application
}