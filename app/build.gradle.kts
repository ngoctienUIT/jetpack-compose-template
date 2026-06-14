import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.firebase.performance)
    alias(libs.plugins.google.services)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

val signingPropertiesFile = rootProject.file("secrets/signing.properties")
val signingProperties = Properties().apply {
    if (signingPropertiesFile.exists()) {
        signingPropertiesFile.inputStream().use { load(it) }
    }
}

fun loadEnvProperties(flavorName: String): Properties {
    val envFile = rootProject.file("secrets/env.$flavorName.properties")
    val exampleFile = rootProject.file("secrets/env.$flavorName.properties.example")

    val fileToLoad = when {
        envFile.exists() -> envFile
        exampleFile.exists() -> {
            logger.warn(
                "secrets/env.$flavorName.properties not found — " +
                    "using secrets/env.$flavorName.properties.example defaults",
            )
            exampleFile
        }
        else -> error(
            "Missing secrets/env.$flavorName.properties. " +
                "Copy secrets/env.$flavorName.properties.example and fill in your values.",
        )
    }

    return Properties().apply {
        fileToLoad.inputStream().use { load(it) }
    }
}

fun Properties.requireEnvProperty(key: String, flavorName: String): String {
    return getProperty(key)?.trim()?.takeIf { it.isNotEmpty() }
        ?: error("Missing '$key' in secrets/env.$flavorName.properties")
}

fun String.toBuildConfigString(): String = "\"${replace("\\", "\\\\").replace("\"", "\\\"")}\""

private val envPropertiesCache = mutableMapOf<String, Properties>()

fun resolveActiveFlavors(): Set<String>? {
    val taskNames = gradle.startParameter.taskNames
    if (taskNames.isEmpty()) return null

    val flavors = mutableSetOf<String>()
    for (task in taskNames) {
        val normalized = task.substringAfterLast(':').lowercase()
        when {
            "staging" in normalized -> flavors.add("staging")
            "production" in normalized -> flavors.add("production")
        }
    }
    // Tasks like `build` or `clean` don't name a flavor — load all env files.
    return flavors.takeIf { it.isNotEmpty() }
}

fun envForFlavor(flavorName: String): Properties {
    return envPropertiesCache.getOrPut(flavorName) {
        val activeFlavors = resolveActiveFlavors()
        if (activeFlavors != null && flavorName !in activeFlavors) {
            Properties().apply {
                setProperty("API_BASE_URL", "https://unused.local/")
            }
        } else {
            loadEnvProperties(flavorName)
        }
    }
}

android {
    namespace = "com.ngoctientnt.template"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.ngoctientnt.template"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            if (signingPropertiesFile.exists()) {
                storeFile = rootProject.file(signingProperties.getProperty("storeFile"))
                storePassword = signingProperties.getProperty("storePassword")
                keyAlias = signingProperties.getProperty("keyAlias")
                keyPassword = signingProperties.getProperty("keyPassword")
            }
        }
    }

    flavorDimensions += "environment"
    productFlavors {
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            resValue("string", "app_name", "Template Staging")
            buildConfigField("String", "ENVIRONMENT", "\"staging\"")
            buildConfigField(
                "String",
                "API_BASE_URL",
                envForFlavor("staging").requireEnvProperty("API_BASE_URL", "staging").toBuildConfigString(),
            )
        }
        create("production") {
            dimension = "environment"
            resValue("string", "app_name", "Template")
            buildConfigField("String", "ENVIRONMENT", "\"production\"")
            buildConfigField(
                "String",
                "API_BASE_URL",
                envForFlavor("production").requireEnvProperty("API_BASE_URL", "production").toBuildConfigString(),
            )
        }
    }

    buildTypes {
        debug {
            versionNameSuffix = "-debug"
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = when {
                signingPropertiesFile.exists() -> signingConfigs.getByName("release")
                else -> signingConfigs.getByName("debug")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        buildConfig = true
        compose = true
        resValues = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.firebase.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.compose.adaptive.navigation)

    // Retrofit
    implementation(libs.hilt.android)
    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)

    // Room — dependency ready; add @Database when implementing local cache
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto)

    implementation(libs.coil.compose)

    // Paging — dependency ready; wire when implementing paginated lists
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    // Firebase
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.config)
    implementation(libs.firebase.perf)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
