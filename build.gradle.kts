plugins {
    java
    kotlin("multiplatform")
    //id("org.jetbrains.kotlinx.benchmark") version "0.4.9"
}

repositories {
    mavenCentral()
}

//benchmark {
//    configurations {
//        named("main") {
//            iterationTime = 5
//            iterationTimeUnit = "sec"
//        }
//    }
//    targets {
//        register("main") {
//            this as kotlinx.benchmark.gradle.JvmBenchmarkTarget
//            jmhVersion = "1.21"
//        }
//    }
//}

tasks.test {
    useJUnitPlatform()
}