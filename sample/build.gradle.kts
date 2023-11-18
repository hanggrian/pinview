val releaseArtifact: String by project

plugins {
    alias(libs.plugins.android.application)
    kotlin("android") version libs.versions.kotlin
}

kotlin.jvmToolchain(libs.versions.jdk.get().toInt())

android {
    namespace = "com.example"
    testNamespace = "$namespace.test"
    defaultConfig {
        applicationId = namespace
        multiDexEnabled = true
    }
    lint.abortOnError = false
    kotlinOptions.jvmTarget = JavaVersion.toVersion(libs.versions.jdk.get()).toString()
}

dependencies {
    implementation(project(":$releaseArtifact"))
    implementation(libs.material)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.androidx.preference)
    implementation(libs.process.phoenix)
}
