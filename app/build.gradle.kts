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

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}

val signingPropertiesFile = rootProject.file("secrets/signing.properties")
val signingProperties = Properties().apply {
    if (signingPropertiesFile.exists()) {
        signingPropertiesFile.inputStream().use { load(it) }
    }
}

fun loadBrandProperties(): Properties {
    val brandFile = rootProject.file("config/brand.properties")
    val exampleFile = rootProject.file("config/brand.properties.example")
    val fileToLoad = brandFile.takeIf { it.exists() }
        ?: exampleFile.takeIf { it.exists() }
        ?: error(
            "Missing config/brand.properties. " +
                "Run ./scripts/brand init and edit config/brand.properties.",
        )

    if (!brandFile.exists()) {
        logger.warn(
            "config/brand.properties not found — using config/brand.properties.example defaults",
        )
    }

    return Properties().apply {
        fileToLoad.inputStream().use { load(it) }
    }
}

data class BrandConfig(
    val applicationId: String,
    val appNameProduction: String,
    val appNameStaging: String,
    val stagingApplicationIdSuffix: String,
    val stagingVersionNameSuffix: String,
    val splashDelayMs: Long,
    val imageDiskCacheDir: String,
) {
    val stagingApplicationId: String
        get() = applicationId + stagingApplicationIdSuffix
}

fun brandConfig(): BrandConfig {
    val props = loadBrandProperties()
    fun required(key: String): String =
        props.getProperty(key)?.trim()?.takeIf { it.isNotEmpty() }
            ?: error("Missing '$key' in brand config")

    fun optional(key: String, default: String): String =
        props.getProperty(key)?.trim()?.takeIf { it.isNotEmpty() } ?: default

    return BrandConfig(
        applicationId = required("application_id"),
        appNameProduction = required("app_name_production"),
        appNameStaging = required("app_name_staging"),
        stagingApplicationIdSuffix = optional("staging_application_id_suffix", ".staging"),
        stagingVersionNameSuffix = optional("staging_version_name_suffix", "-staging"),
        splashDelayMs = optional("splash_delay_ms", "1500").toLong(),
        imageDiskCacheDir = optional("image_disk_cache_dir", "image_cache"),
    )
}

val brand = brandConfig()

fun loadEnvProperties(flavorName: String): Properties {
    val envFile = rootProject.file("secrets/env.$flavorName.properties")
    val exampleFile = rootProject.file("secrets/env.$flavorName.properties.example")
    val fileToLoad = envFile.takeIf { it.exists() }
        ?: exampleFile.takeIf { it.exists() }
        ?: error(
            "Missing secrets/env.$flavorName.properties. " +
                "Copy secrets/env.$flavorName.properties.example and fill in your values.",
        )

    if (!envFile.exists()) {
        logger.warn(
            "secrets/env.$flavorName.properties not found — " +
                "using secrets/env.$flavorName.properties.example defaults",
        )
    }

    return Properties().apply {
        fileToLoad.inputStream().use { load(it) }
    }
}

fun String.toBuildConfigString(): String = "\"${replace("\\", "\\\\").replace("\"", "\\\"")}\""

data class EnvConfig(
    val apiBaseUrl: String,
    val googleWebClientId: String,
    val facebookAppId: String,
    val facebookClientToken: String,
) {
    val facebookAppIdRes: String get() = facebookAppId.ifBlank { "0" }
    val facebookClientTokenRes: String get() = facebookClientToken.ifBlank { "0" }
    val fbLoginProtocolScheme: String get() = "fb$facebookAppIdRes"

    companion object {
        fun placeholder() = EnvConfig(
            apiBaseUrl = "https://unused.local/",
            googleWebClientId = "",
            facebookAppId = "",
            facebookClientToken = "",
        )
    }
}

fun envConfig(flavorName: String): EnvConfig {
    val props = loadEnvProperties(flavorName)
    fun optional(key: String, default: String = ""): String {
        return props.getProperty(key)?.trim()?.takeIf { it.isNotEmpty() } ?: default
    }
    fun required(key: String): String {
        return optional(key).ifEmpty {
            error("Missing '$key' in secrets/env.$flavorName.properties")
        }
    }

    return EnvConfig(
        apiBaseUrl = required("API_BASE_URL"),
        googleWebClientId = optional("GOOGLE_WEB_CLIENT_ID"),
        facebookAppId = optional("FACEBOOK_APP_ID"),
        facebookClientToken = optional("FACEBOOK_CLIENT_TOKEN"),
    )
}

private val envCache = mutableMapOf<String, EnvConfig>()

fun flavorsInCurrentTask(): Set<String>? =
    gradle.startParameter.taskNames
        .asSequence()
        .map { it.substringAfterLast(':').lowercase() }
        .flatMap { task ->
            buildList {
                if ("staging" in task) add("staging")
                if ("production" in task) add("production")
            }
        }
        .toSet()
        .takeIf { it.isNotEmpty() }

fun envFor(flavor: String): EnvConfig = envCache.getOrPut(flavor) {
    val activeFlavors = flavorsInCurrentTask()
    when {
        activeFlavors == null -> envConfig(flavor)
        flavor in activeFlavors -> envConfig(flavor)
        else -> EnvConfig.placeholder()
    }
}

fun com.android.build.api.dsl.ApplicationProductFlavor.applyEnv(
    environment: String,
    env: EnvConfig,
    appName: String,
    applicationIdSuffix: String? = null,
    versionNameSuffix: String? = null,
) {
    dimension = "environment"
    applicationIdSuffix?.let { this.applicationIdSuffix = it }
    versionNameSuffix?.let { this.versionNameSuffix = it }
    resValue("string", "app_name", appName)
    buildConfigField("String", "ENVIRONMENT", "\"$environment\"")
    buildConfigField("String", "API_BASE_URL", env.apiBaseUrl.toBuildConfigString())
    buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", env.googleWebClientId.toBuildConfigString())
    buildConfigField("String", "FACEBOOK_APP_ID", env.facebookAppId.toBuildConfigString())
    buildConfigField("String", "FACEBOOK_CLIENT_TOKEN", env.facebookClientToken.toBuildConfigString())
    resValue("string", "facebook_app_id", env.facebookAppIdRes)
    resValue("string", "facebook_client_token", env.facebookClientTokenRes)
    resValue("string", "fb_login_protocol_scheme", env.fbLoginProtocolScheme)
}

android {
    namespace = brand.applicationId
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = brand.applicationId
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("long", "SPLASH_DELAY_MS", "${brand.splashDelayMs}L")
        buildConfigField("String", "IMAGE_DISK_CACHE_DIR", brand.imageDiskCacheDir.toBuildConfigString())

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
            applyEnv(
                environment = "staging",
                env = envFor("staging"),
                appName = brand.appNameStaging,
                applicationIdSuffix = brand.stagingApplicationIdSuffix,
                versionNameSuffix = brand.stagingVersionNameSuffix,
            )
        }
        create("production") {
            applyEnv(
                environment = "production",
                env = envFor("production"),
                appName = brand.appNameProduction,
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
    implementation(libs.androidx.core.splashscreen)
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

    // Room — see README_ROOM.md for extending entities, DAOs, and migrations
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    implementation(libs.sqlcipher.android)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.tink)
    // Legacy EncryptedSharedPreferences migration only — see LegacyEncryptedSharedPreferencesMigration.kt
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

    // Social auth
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.facebook.login)

    // Test
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.arch.core.testing)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

tasks.register<Exec>("brandValidate") {
    group = "brand"
    description = "Validate brand config, Firebase package names, and assets."
    workingDir = rootProject.projectDir
    commandLine("python3", "scripts/brand_lib/cli.py", "validate")
}

tasks.register<Exec>("brandApplyIdentity") {
    group = "brand"
    description = "Apply brand identity (package rename, theme, settings)."
    workingDir = rootProject.projectDir
    val dryRun = project.findProperty("brandDryRun")?.toString() == "true"
    commandLine(
        "python3",
        "scripts/brand_lib/cli.py",
        "apply",
        "identity",
        *(if (dryRun) arrayOf("--dry-run") else emptyArray()),
    )
}
