val RELEASE_ARTIFACT: String by project

plugins {
    alias(libs.plugins.android.application)
    kotlin("android") version libs.versions.kotlin
}

kotlin.jvmToolchain(libs.versions.jdk.get().toInt())

android {
    kotlinOptions.jvmTarget = JavaVersion.toVersion(libs.versions.jdk.get()).toString()
    namespace = "com.example.$RELEASE_ARTIFACT"
    testNamespace = "$namespace.test"
    defaultConfig {
        applicationId = namespace
        multiDexEnabled = true
    }
    lint.abortOnError = false
}

dependencies {
    implementation(project(":$RELEASE_ARTIFACT"))
    implementation(libs.material)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.androidx.preference)
    implementation(libs.process.phoenix)
}
