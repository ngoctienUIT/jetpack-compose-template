# Template

Android application template built with **Kotlin**, **Jetpack Compose**, and **Material Design 3**. Designed as a reusable starter for new projects — includes navigation, theming, localization, networking, Firebase, shared UI components, device metadata, and image loading out of the box.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Requirements](#requirements)
- [Getting Started](#getting-started)
- [Build Variants](#build-variants)
- [Project Structure](#project-structure)
- [Architecture](#architecture)
- [Navigation](#navigation)
- [Theming & Localization](#theming--localization)
- [Shared UI Components](#shared-ui-components)
- [App & Device Info](#app--device-info)
- [Networking](#networking)
- [Image Loading](#image-loading)
- [Firebase & Push Notifications](#firebase--push-notifications)
- [Connectivity Monitoring](#connectivity-monitoring)
- [Dependency Injection](#dependency-injection)
- [Central Configuration](#central-configuration)
- [Production Setup](#production-setup)
- [Customization Guide](#customization-guide)
- [Gradle Commands](#gradle-commands)
- [Roadmap / TODO](#roadmap--todo)

---

## Overview

| Item | Value |
|------|-------|
| **Package** | `com.ngoctientnt.template` |
| **Min SDK** | 24 (Android 7.0) |
| **Target SDK** | 36 |
| **Compile SDK** | 37 |
| **Language** | Kotlin 2.3 |
| **UI** | Jetpack Compose + Material 3 |
| **DI** | Hilt 2.59 |
| **Gradle** | 9.4.1 |
| **AGP** | 9.2.1 |

This template follows a **feature-first package structure** inside a single `app` module, with shared logic in `core/`, reusable UI in `ui/`, and Hilt modules in `di/`. It is optimized for fast project bootstrapping and easy visual/behavior customization per client or product.

---

## Features

### App Shell
- Splash screen with placeholder auth routing
- Login screen (demo UI with shared form components)
- Main shell with **persistent bottom tabs** (state preserved when switching tabs)
- Custom **notched bottom navigation bar** with center Favorite action
- Full-screen Favorite route (outside tab host)
- Detail screen with typed route arguments

### Developer Experience
- **Product flavors**: `staging` / `production` with separate `applicationId`, API URL, and app name
- **Release hardening**: R8 minify + shrink resources + ProGuard rules for Retrofit, Hilt, Coil, Serialization
- **Signing template**: `secrets/signing.properties.example` — never commit real keystores
- **BuildConfig** fields: `ENVIRONMENT`, `API_BASE_URL`, `BUILD_TYPE`
- **Central config layer**: `ConfigModule` + `AppConfig` — single place to customize clone-time behavior
- Type-safe navigation via **Navigation 3** + Kotlin Serialization
- Centralized **shared UI component library** with global theme override
- **AppInfoManager** — device/app metadata (Hilt + nullable `LocalAppInfo`)
- Configurable HTTP headers via `AppInfoConfig` / `NetworkConfig` (privacy-safe defaults)
- Coil image loader sharing OkHttp client with Retrofit
- **AppLogger** — debug-only logging (no tokens/PII in release logcat)

### Platform Integrations
- Firebase Analytics, Cloud Messaging, Remote Config, Performance Monitoring
- Push notification deep-link routing (`home`, `profile`, `detail`)
- Network connectivity observer with global no-internet dialog
- Per-app language (English / Vietnamese / System) via AppCompat locales
- Light / Dark / System theme with animated color transitions
- DataStore Preferences for locale, theme, and device UDID persistence

---

## Tech Stack

| Category | Libraries |
|----------|-----------|
| **UI** | Jetpack Compose BOM, Material 3, Material Icons Extended |
| **Navigation** | Navigation 3, Adaptive Navigation |
| **Architecture** | Hilt, ViewModel, Lifecycle, Coroutines, StateFlow |
| **Network** | Retrofit 3, OkHttp 5, Kotlinx Serialization JSON |
| **Local Storage** | Room (dependency only), DataStore Preferences |
| **Images** | Coil 2.7 (Compose) |
| **Paging** | Paging 3 (runtime + compose) — dependency only, no implementation yet |
| **Firebase** | Analytics, Messaging, Remote Config (dependency only), Performance |
| **Testing** | JUnit 4, Espresso, Compose UI Test |

---

## Requirements

- **Android Studio** Ladybug or newer (recommended)
- **JDK 17**
- **Android SDK** with API 37
- **Firebase project** with `google-services.json` per flavor (see [Getting Started](#getting-started))

---

## Getting Started

### 1. Clone the repository

```bash
git clone <repository-url>
cd Template
```

### 2. Configure Firebase

Place your Firebase config files:

```
app/src/staging/google-services.json      # com.ngoctientnt.template.staging
app/src/production/google-services.json   # com.ngoctientnt.template
```

Each flavor uses a different `applicationId`. Register both package names in the Firebase console.

> **Important:** Replace the bundled `google-services.json` files with configs from **your own** Firebase project before shipping. Do not use template Firebase projects in production.

### 3. Configure release signing (for Play Store builds)

```bash
cp secrets/signing.properties.example secrets/signing.properties
# Edit secrets/signing.properties with your keystore path and credentials
```

Release builds use R8 minification. Without `signing.properties`, release APKs fall back to the debug keystore (local testing only).

### 4. Sync & Run

Open the project in Android Studio, sync Gradle, then run:

| Run configuration | Description |
|-------------------|-------------|
| `stagingDebug` | Dev/staging build (`Template Staging`) |
| `productionDebug` | Production package debug build |
| `stagingRelease` / `productionRelease` | Release builds |

Or from terminal:

```bash
./gradlew :app:assembleStagingDebug
./gradlew :app:installStagingDebug
```

### 5. First launch flow

```
SplashScreen (1.5s)
    └── MainRoute (Home tab)   ← current default
    └── LoginRoute             ← TODO: wire auth check in SplashScreen
```

---

## Build Variants

Two dimensions combine into four main variants:

| Flavor | Build Type | Application ID | App Name | API Base URL |
|--------|------------|----------------|----------|--------------|
| staging | debug/release | `com.ngoctientnt.template.staging` | Template Staging | `https://staging.example.com/` |
| production | debug/release | `com.ngoctientnt.template` | Template | `https://api.example.com/` |

**Release build type:** `isMinifyEnabled = true`, `isShrinkResources = true`, ProGuard rules in `app/proguard-rules.pro`.

**Debug build type:** no minification, `versionNameSuffix = "-debug"`.

Configure in `app/build.gradle.kts`:

```kotlin
productFlavors {
    create("staging") {
        buildConfigField("String", "ENVIRONMENT", "\"staging\"")
        buildConfigField("String", "API_BASE_URL", "\"https://staging.example.com/\"")
    }
    create("production") {
        buildConfigField("String", "ENVIRONMENT", "\"production\"")
        buildConfigField("String", "API_BASE_URL", "\"https://api.example.com/\"")
    }
}
```

Access in code:

```kotlin
BuildConfig.ENVIRONMENT   // "staging" | "production"
BuildConfig.API_BASE_URL
BuildConfig.BUILD_TYPE      // "debug" | "release"
```

---

## Project Structure

```
Template/
├── app/
│   └── src/main/
│       ├── java/com/ngoctientnt/template/
│       │   ├── TemplateApplication.kt       # Hilt entry + ApplicationBootstrap
│       │   ├── MainActivity.kt              # Compose host, notification permission
│       │   │
│       │   ├── app/                         # App-level composition
│       │   │   ├── App.kt                   # Root composable, LocalAppInfo provider
│       │   │   └── navigation/              # Navigation 3 setup
│       │   │       ├── Route.kt             # Serializable routes
│       │   │       ├── AppNavHost.kt        # NavDisplay + screen entries
│       │   │       ├── AppNavigator.kt      # navigate / replace / pop API
│       │   │       ├── AppBackStack.kt      # Back stack wrapper
│       │   │       ├── BottomNavTab.kt        # Tab enum + icons
│       │   │       └── LocalAppNavigator.kt # Compose access
│       │   │
│       │   ├── core/                        # Business infrastructure
│       │   │   ├── appinfo/                 # Device + package + UDID
│       │   │   ├── config/                  # AppConfig, NetworkConfig, NotificationRoutes
│       │   │   ├── logging/                 # AppLogger (debug-only)
│       │   │   ├── startup/                 # ApplicationBootstrap
│       │   │   ├── locale/                  # Language persistence
│       │   │   ├── theme/                   # Theme mode persistence
│       │   │   ├── network/                 # Connectivity observer
│       │   │   └── notification/            # FCM, channels, deep links
│       │   │
│       │   ├── di/                          # Hilt modules
│       │   │   ├── ConfigModule.kt          # ← main clone entry point
│       │   │   ├── NetworkModule.kt
│       │   │   ├── NavigationModule.kt
│       │   │   ├── ImageModule.kt
│       │   │   └── CoroutineModule.kt
│       │   │
│       │   ├── feature/                     # Feature screens + ViewModels
│       │   │   ├── splash/
│       │   │   ├── login/
│       │   │   ├── main/                    # Tab shell + MainViewModel
│       │   │   ├── home/
│       │   │   ├── explore/
│       │   │   ├── activity/
│       │   │   ├── profile/
│       │   │   ├── favorite/
│       │   │   ├── detail/
│       │   │   ├── theme/                   # ThemeViewModel
│       │   │   ├── locale/                  # LocaleViewModel
│       │   │   └── connectivity/            # ConnectivityViewModel
│       │   │
│       │   └── ui/                          # Design system
│       │       ├── theme/                   # Colors, Typography, Shapes, TemplateTheme
│       │       └── component/               # Shared components
│       │           ├── button/              # AppFilledButton, AppOutlinedButton, ...
│       │           ├── input/               # AppTextField
│       │           ├── image/               # AppAsyncImage, AppAvatarImage
│       │           └── theme/               # AppComponentTheme (CompositionLocal)
│       │
│       └── res/
│           ├── values/strings.xml           # English (default)
│           ├── values-vi/strings.xml        # Vietnamese
│           └── xml/locales_config.xml       # Per-app language config
│
├── gradle/libs.versions.toml                # Version catalog
├── build.gradle.kts
└── settings.gradle.kts
```

---

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│  Presentation (feature/*)                               │
│  Composable screens, ViewModels, UI state               │
└───────────────────────┬─────────────────────────────────┘
                        │ uses
┌───────────────────────▼─────────────────────────────────┐
│  Core (core/*)                                          │
│  Managers, Repositories, Providers, Observers           │
└───────────────────────┬─────────────────────────────────┘
                        │ uses
┌───────────────────────▼─────────────────────────────────┐
│  Platform (Android APIs, Firebase, DataStore, Room)     │
└─────────────────────────────────────────────────────────┘
```

### Key patterns

| Pattern | Where | Purpose |
|---------|-------|---------|
| **Manager** | `LocaleManager`, `ThemeManager`, `AppInfoManager` | High-level API over repositories |
| **Repository** | `LocaleRepository`, `ThemeRepository`, `AppInfoRepository` | Data access + persistence |
| **Provider** | `DeviceInfoProvider`, `PackageInfoProvider` | Platform data reading |
| **CompositionLocal** | `LocalAppNavigator`, `LocalAppInfo`, `LocalAppComponentTheme` | Implicit context in Compose |
| **ViewModel** | Feature ViewModels | UI state, survives config changes |
| **Singleton + Hilt** | Observers, loaders, navigators | App-scoped services |

---

## Navigation

Built on **Navigation 3** with `@Serializable` route types.

### Routes

| Route | Type | Screen |
|-------|------|--------|
| `SplashRoute` | `data object` | SplashScreen |
| `LoginRoute` | `data object` | LoginScreen |
| `MainRoute(tab)` | `data class` | MainScreen with bottom tabs |
| `DetailRoute(id)` | `data class` | DetailScreen |
| `FavoriteRoute` | `data object` | FavoriteScreen (full-screen) |

### Navigator API

Inject or use `LocalAppNavigator.current`:

```kotlin
navigator.navigate(DetailRoute(id = "42"))   // push
navigator.replace(LoginRoute)                  // replace top
navigator.replaceAll(MainRoute())              // clear stack + set root
navigator.pop()                                // go back
navigator.popToRoot()
```

### Bottom tabs

`MainScreen` uses a **persistent tab host** — all four tabs stay composed; visibility toggles via alpha/zIndex so scroll position and state are preserved.

| Tab | Screen |
|-----|--------|
| HOME | HomeScreen |
| EXPLORE | ExploreScreen |
| ACTIVITY | ActivityScreen |
| PROFILE | ProfileScreen |

Center **Favorite** button on the notched bottom bar navigates to `FavoriteRoute`.

Back press on non-Home tabs returns to Home (does not exit app).

---

## Theming & Localization

### Theme modes

Managed by `ThemeViewModel` + `ThemeRepository` (DataStore).

| Mode | Behavior |
|------|----------|
| System | Follows OS dark/light setting |
| Light | Always light |
| Dark | Always dark |

`TemplateTheme` applies Material 3 color scheme with optional animated transitions (`ThemeTransition.kt`).

Change theme in **Profile → Appearance**.

### Languages

Managed by `LocaleViewModel` + `LocaleRepository` (DataStore).

| Language | Resource folder |
|----------|-----------------|
| System default | — |
| English | `values/strings.xml` |
| Vietnamese | `values-vi/strings.xml` |

**Adding a new language:**

1. Add entry to `AppLanguage` enum (`core/locale/AppLanguage.kt`)
2. Create `res/values-<tag>/strings.xml`
3. Add display name string to all `strings.xml` files

Change language in **Profile → Language**.

---

## Shared UI Components

All shared components read defaults from `AppComponentTheme` via `LocalAppComponentTheme`. Override globally in one place or per-component via parameters.

### Theme setup

`TemplateTheme` automatically wraps content with `ProvideAppComponentTheme`. Customize at app root:

```kotlin
TemplateTheme(
    themeMode = themeMode,
    componentTheme = AppComponentDefaults.pillTheme(), // preset
    // or:
    componentTheme = AppComponentDefaults.theme(
        buttonShape = RoundedCornerShape(20.dp),
        imageShape = RoundedCornerShape(16.dp),
        imagePlaceholderColor = Color(0xFFECECEC),
    ),
) { ... }
```

### Buttons (`ui/component/button/AppButtons.kt`)

| Component | Material 3 equivalent |
|-----------|----------------------|
| `AppFilledButton` | `Button` |
| `AppElevatedButton` | `ElevatedButton` |
| `AppOutlinedButton` | `OutlinedButton` |
| `AppTextButton` | `TextButton` |

Shared parameters: `loading`, `fullWidth`, `size` (`Small` / `Medium` / `Large`), `leadingIcon`, `trailingIcon`, `colors`, `shape`.

```kotlin
AppFilledButton(
    text = "Sign in",
    onClick = { },
    fullWidth = true,
    loading = isLoading,
    size = AppButtonSize.Large,
)
```

### Text Input (`ui/component/input/AppTextField.kt`)

Wrapper around Material 3 `OutlinedTextField` with shared styling.

```kotlin
AppTextField(
    value = email,
    onValueChange = { email = it },
    label = "Email",
    isError = emailError != null,
    supportingText = emailError,
)
```

### Images (`ui/component/image/`)

| Component | Use case |
|-----------|----------|
| `AppAsyncImage` | Remote images (banners, thumbnails, cards) |
| `AppAvatarImage` | Circular cropped avatars |

Built-in loading spinner, error icon, empty state, crossfade, shape clipping.

```kotlin
AppAsyncImage(
    url = imageUrl,
    contentDescription = title,
    modifier = Modifier
        .fillMaxWidth()
        .height(200.dp),
)

AppAvatarImage(
    url = user.avatarUrl,
    contentDescription = user.name,
    size = 56.dp,
)
```

### Other UI

| Component | Location |
|-----------|----------|
| `NotchedBottomNavigationBar` | Custom bottom bar with center FAB notch |
| `LanguageSelector` | Radio list + apply button |
| `ThemeSelector` | Radio list + apply button |
| `NoInternetDialog` | Global connectivity dialog |

---

## App & Device Info

Equivalent to Flutter's `device_info_plus`, `package_info_plus`, and `flutter_udid`.

### Models

| Model | Contents |
|-------|----------|
| `DeviceInfo` | brand, model, manufacturer, OS version, SDK, androidId, isPhysicalDevice, screen metrics, locale |
| `AppPackageInfo` | appName, packageName, versionName, versionCode, buildType, environment |
| `AppUdid` | Persistent UUID (generated once, stored in DataStore) |
| `AppInfo` | Aggregates all three + `toHttpHeaders()` / `toUserAgent()` |

### Usage

**Hilt injection (ViewModel, Service, Repository):**

```kotlin
@Inject lateinit var appInfoManager: AppInfoManager

val udid = appInfoManager.udid?.value
val version = appInfoManager.packageInfo?.versionName
val headers = appInfoManager.toHttpHeaders()   // suspend
```

**Compose (nullable until async init completes):**

```kotlin
val appInfo = LocalAppInfo.current

appInfo?.let {
    Text("UDID: ${it.udid.value}")
    Text("${it.device.model} · Android ${it.device.osVersion}")
}
```

### Configuration

Override in `di/ConfigModule.kt`:

```kotlin
fun provideAppInfoConfig() = AppInfoConfig(
    includeAndroidId = false,       // privacy-safe default
    includeHttpHeaders = true,
    includeUdidInHeaders = true,
)
```

Initialized via `ApplicationBootstrap` — locale/theme sync on startup, app-info loads asynchronously.

---

## Networking

### Stack

- **Retrofit 3** + Kotlinx Serialization converter
- **OkHttp 5** with logging (BODY in debug, NONE in release)
- **`AppInfoInterceptor`** — attaches device/app headers automatically

### Setup

Base URL comes from `BuildConfig.API_BASE_URL` per flavor.

To add an API service:

```kotlin
// di/NetworkModule.kt
@Provides
@Singleton
fun provideUserApi(retrofit: Retrofit): UserApi =
    retrofit.create(UserApi::class.java)
```

```kotlin
interface UserApi {
    @GET("users/me")
    suspend fun getMe(): UserDto
}
```

Room and Paging dependencies are included and ready for local cache + paginated lists.

---

## Image Loading

Coil `ImageLoader` is provided via Hilt and registered on `TemplateApplication` (`ImageLoaderFactory`).

- Shares **OkHttp client** with Retrofit (same cookies, interceptors, TLS)
- Memory cache: 25% of app memory
- Disk cache: 2% of disk in `cacheDir/image_cache`
- Debug logging enabled in debug builds

Configure in `di/ConfigModule.kt`:

```kotlin
fun provideAppImageLoaderConfig() = AppImageLoaderConfig(
    crossfadeDurationMillis = 500,
    memoryCacheMaxSizePercent = 0.25,
    diskCacheMaxSizePercent = 0.02,
)
```

---

## Firebase & Push Notifications

### Included services

| Service | Class / Usage |
|---------|---------------|
| FCM | `AppFirebaseMessagingService` |
| Token | `FcmTokenManager.fetchToken()` on app start |
| Display | `NotificationHelper` — channel, large icon, big picture |
| Deep links | `NotificationRouteHandler` |

### Notification routing

Push data payload keys (defined in `core/config/NotificationRoutes.kt`):

| `route` value | Navigation |
|---------------|------------|
| `home` | `MainRoute(HOME)` |
| `profile` | `MainRoute(PROFILE)` |
| `detail` | `DetailRoute(id)` — requires `id` extra |

### Permissions

`POST_NOTIFICATIONS` is requested at runtime on Android 13+ in `MainActivity`.

---

## Connectivity Monitoring

`NetworkConnectivityObserver` exposes `StateFlow<NetworkStatus>`:

| Status | Meaning |
|--------|---------|
| `Available` | Internet capability validated |
| `Unavailable` | No network or not validated |

`ConnectivityViewModel` + `NoInternetDialog` in `App()` show a blocking retry dialog when offline.

---

## Dependency Injection

Hilt modules in `di/`:

| Module | Provides |
|--------|----------|
| **`ConfigModule`** | **`NetworkConfig`, `AppInfoConfig`, `AppImageLoaderConfig`** ← start here when cloning |
| `NetworkModule` | `Json`, `OkHttpClient`, `Retrofit` |
| `NavigationModule` | `AppBackStack`, `AppNavigator` |
| `ImageModule` | `ImageLoader` |
| `CoroutineModule` | `@ApplicationScope CoroutineScope` |

All `@Singleton` services use constructor injection (`@Inject`) and are auto-discovered by Hilt.

---

## Central Configuration

When cloning this template, customize behavior in these files (in order of priority):

| File | Purpose |
|------|---------|
| **`di/ConfigModule.kt`** | Network timeouts, HTTP logging, app-info header policy, Coil cache |
| **`app/build.gradle.kts`** | `applicationId`, flavors, `API_BASE_URL`, signing |
| **`core/config/AppConfig.kt`** | Splash delay, cache dir names, BuildConfig accessors |
| **`core/config/NotificationRoutes.kt`** | FCM deep-link route constants |
| **`core/config/DataStoreNames.kt`** | DataStore file names (sync with backup exclusions) |
| **`ui/theme/Color.kt`** | Brand color palette |
| **`ui/component/theme/AppComponentDefaults.kt`** | Shared button/input/image styling |

Example — disable metadata headers for a privacy-focused app:

```kotlin
// ConfigModule.kt
fun provideNetworkConfig() = NetworkConfig(
    attachAppInfoHeaders = false,
    enableHttpLogging = BuildConfig.DEBUG,
)

fun provideAppInfoConfig() = AppInfoConfig(
    includeHttpHeaders = false,
)
```

---

## Production Setup

| Concern | Implementation |
|---------|----------------|
| **Code shrinking** | R8 enabled on `release` + `app/proguard-rules.pro` |
| **Resource shrinking** | `isShrinkResources = true` on release |
| **Signing** | `secrets/signing.properties` (see `.example`) |
| **Network security** | `res/xml/network_security_config.xml` — HTTPS only by default |
| **Backup policy** | DataStore folder excluded in `backup_rules.xml` |
| **Logging** | `AppLogger` — no-op in release builds |
| **Privacy defaults** | `includeAndroidId = false`, FCM token not logged in release |
| **Startup** | `ApplicationBootstrap` — locale/theme sync, app-info async |

See **[TEMPLATE.md](TEMPLATE.md)** for the full step-by-step clone checklist.

---

## Customization Guide

See **[TEMPLATE.md](TEMPLATE.md)** for the complete ordered checklist. Quick summary:

### 1. Identity

- [ ] Change `applicationId` and `namespace` in `app/build.gradle.kts`
- [ ] Refactor package `com.ngoctientnt.template`
- [ ] Replace `google-services.json` per flavor with your Firebase project
- [ ] Replace launcher icons in `res/mipmap-*`

### 2. Configuration (single entry point)

- [ ] Edit `di/ConfigModule.kt` for network, app-info, and image settings
- [ ] Edit `core/config/AppConfig.kt` for app-level constants
- [ ] Edit `app/build.gradle.kts` flavors for API URLs

### 3. Branding

- [ ] Edit colors in `ui/theme/Color.kt`
- [ ] Adjust `AppComponentDefaults` / `TemplateTheme` presets

### 4. Release

- [ ] Set up `secrets/signing.properties`
- [ ] Verify `./gradlew :app:assembleProductionRelease`
- [ ] Restrict Firebase API keys in Google Cloud console

### 5. Features

- [ ] Wire auth in `SplashScreen`
- [ ] Add Retrofit API interfaces in `NetworkModule`
- [ ] Add Room schema when needed (dependency is ready)
- [ ] Send FCM token to backend in `FcmTokenManager`

---

## Gradle Commands

```bash
# Build
./gradlew :app:assembleStagingDebug
./gradlew :app:assembleProductionRelease

# Install on connected device
./gradlew :app:installStagingDebug

# Verify release build (R8 + shrink)
./gradlew :app:assembleProductionRelease

# Tests
./gradlew :app:testStagingDebugUnitTest
./gradlew :app:connectedStagingDebugAndroidTest

# Clean
./gradlew clean
```

---

## Roadmap / TODO

- [ ] Authentication flow — check session in `SplashScreen`, route to `LoginRoute`
- [ ] Room database schema and repositories (dependency wired, no entities yet)
- [ ] Paging integration example (dependency wired)
- [ ] Remote Config usage example
- [ ] Crash reporting integration (Crashlytics / Sentry)
- [ ] Unit / UI tests for shared components
- [x] ProGuard/R8 rules for release minification

---

## Agent Skills (Cursor)

This repo includes Cursor agent skills under `.agents/skills/` and `.cursor/skills/`:

| Skill | Purpose |
|-------|---------|
| `mobile-android-design` | Material 3 + Compose UI patterns |
| `android-clean-architecture` | Module structure, UseCases, Repositories |

These guide AI-assisted development inside Cursor IDE.

---

## License

Private / internal template — add your license here before open-sourcing.
