import com.android.build.gradle.ProguardFiles.getDefaultProguardFile
import org.gradle.kotlin.dsl.kotlin
import org.gradle.model.internal.core.ModelNodes.all

plugins {
    android("application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(SDK_TARGET)
    buildToolsVersion(BUILD_TOOLS)
    defaultConfig {
        minSdkVersion(SDK_MIN)
        targetSdkVersion(SDK_TARGET)
        applicationId = "$RELEASE_GROUP.demo"
        versionCode = 1
        versionName = VERSION_ANDROIDX
    }
    sourceSets {
        getByName("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.srcDirs("src")
            assets.srcDirs("assets")
            res.srcDir("res")
            resources.srcDir("src")
        }
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib", VERSION_KOTLIN))
    implementation(project(":$RELEASE_ARTIFACT"))

    implementation(material())
    implementation(androidx("core", "core-ktx"))
    implementation(androidx("appcompat"))
    implementation(androidx("coordinatorlayout"))
    implementation(androidx("preference"))
}