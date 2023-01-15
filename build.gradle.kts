import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.yoloroy"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.apply {
                jvmTarget = "11"
                freeCompilerArgs += "-Xcontext-receivers"
            }
        }
        withJava()
    }
    sourceSets {
        all {
            languageSettings.optIn("androidx.compose.ui.ExperimentalComposeUiApi")
            languageSettings.optIn("androidx.compose.material.ExperimentalMaterialApi")
            languageSettings.optIn("androidx.compose.foundation.ExperimentalFoundationApi")
        }
        val commonMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("${compose.ui}.input.pointer")
                implementation(project("algorithm_lib"))
                implementation(project("tree_saver"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Algos_course_work_AVL_tree"
            packageVersion = "1.0.0"
        }
    }
}
