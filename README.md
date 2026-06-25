# Android Production Template

A high-performance, production-ready Android application template built with **Kotlin**, **Jetpack Compose**, and **Material Design 3**. This project is designed as a robust foundational layer for senior developers to bootstrap new projects in minutes, not days.

---

## 🎯 Project Philosophy

- **Zero-Boilerplate**: Standardized Base classes and automated code generation for features.
- **Production Hardened**: Pre-configured R8/Proguard rules, secure networking, and privacy-first defaults.
- **Highly Scalable**: Feature-first package structure (MVI) that scales with complex business logic.
- **Automation Driven**: One-command rebranding pipeline (Package Name, Icons, Colors, Firebase).
- **Security First**: All sensitive local data is encrypted using **Jetpack DataStore + Google Tink**.

---

## 🛠 Tech Stack & Architecture

### Core Architecture
- **Architecture**: **MVI (Model-View-Intent)** using a custom `BaseViewModel`.
- **UI Framework**: **Jetpack Compose** with **Material 3**.
- **Dependency Injection**: **Hilt** 2.59.
- **Navigation**: **Navigation 3** + Kotlin Serialization (Type-safe, entry-based navigation).
- **Concurrency**: Kotlin Coroutines & Flow.

### Networking & Data
- **Networking**: Retrofit 3 + OkHttp 5 + Kotlinx Serialization.
- **Security**: **Google Tink** AEAD encryption for sensitive DataStore files.
- **Local Storage**: DataStore Preferences + **Room** (Encrypted via SQLCipher in Release). See [README_ROOM.md](README_ROOM.md).
- **Image Loading**: Coil 2.7 (optimized with shared OkHttp client).

### Platform & DevEx
- **Firebase**: Analytics, Messaging (FCM), Remote Config, Performance.
- **Connectivity**: Real-time `NetworkConnectivityObserver` with global "No Internet" UI.
- **Localization**: Per-app language support (En/Vi) via `AppCompatDelegate`.
- **Edge-to-Edge**: Fully implemented using `enableEdgeToEdge()` and WindowInsets.

---

## 🚀 Getting Started

### 1. Requirements
- **Android Studio** Ladybug or newer.
- **JDK 17**.
- **Android SDK** API 37.

### 2. Initialization
```bash
# Clone the repository
git clone <repository-url>
cd Template

# Initialize your brand configuration and environment secrets
make brand-init
```

### 3. Identity Configuration
1.  Edit `config/brand.properties`: Set `application_id`, `app_name`, and brand constants.
2.  Add your assets: Place icons in `brand/assets/icons/` and splash logo in `brand/assets/splash/`.
3.  Run the rebranding pipeline:
    ```bash
    make brand-apply
    ```
    *This script renames packages, updates the Manifest, and generates the theme palette.*

### 4. API & Firebase
1.  Configure `secrets/env.{staging|production}.properties` with your `API_BASE_URL`.
2.  Place `google-services.json` in `brand/assets/firebase/{staging|production}/`.
3.  Run `make brand-apply` again to sync Firebase configurations.

---

## 🏗 Architecture & Patterns

### BaseViewModel (MVI)
All features should extend `BaseViewModel<State, Intent, Effect>`. This ensures a predictable, unidirectional data flow.

- **State**: Persistent UI state (e.g., `data class LoginState(...)`).
- **Intent**: User actions or events (e.g., `sealed class LoginIntent`).
- **Effect**: One-time side effects like navigation or toasts (e.g., `sealed class LoginEffect`).

Example usage in Screen:
```kotlin
val state by viewModel.uiState.collectAsStateWithLifecycle()
LaunchedEffect(Unit) {
    viewModel.effect.collect { effect -> /* Handle navigation/toast */ }
}
```

### Feature Scaffolding
Generate a new MVI feature module in seconds:
```bash
make gen-feature
```
This creates the Screen, ViewModel, and Contract (State/Intent/Effect) files automatically.

---

## 📦 Project Structure

```
Template/
├── app/                        # Main Android module
│   ├── src/main/java/.../
│   │   ├── app/                # Application class, Navigation & Global DI
│   │   ├── core/               # Shared logic (Network, Security, Database, Architecture)
│   │   ├── di/                 # Feature-specific Hilt modules
│   │   ├── feature/            # Feature-based screens (Home, Auth, Profile, etc.)
│   │   └── ui/                 # Design system (Theme, Components, AppToast)
├── scripts/                    # Automation (Branding, Feature Gen)
├── config/                     # Brand identity configuration
├── secrets/                    # API Keys & Env (git-ignored)
└── brand/                      # Raw assets (Icons, Firebase JSONs)
```

---

## 🔐 Security & Privacy

- **Encryption**: Sensitive local data (Auth Tokens, Passphrases) uses `EncryptedPreferencesStoreFactory` which leverages **Google Tink** and **Jetpack DataStore**.
- **Room**: Release builds use **SQLCipher** for full database encryption.
- **Backup Rules**: `backup_rules.xml` and `data_extraction_rules.xml` are pre-configured to exclude sensitive data and internal databases from cloud backups.
- **Networking**: `network_security_config.xml` enforces TLS 1.3 and prevents cleartext traffic.

---

## 🔧 Automation Commands

| Command | Description |
|---------|-------------|
| `make brand-init` | Scaffold project config and secrets |
| `make brand-apply` | Apply Package Name, App Name, Icons, and Splash |
| `make gen-feature` | Generate MVI boilerplate for a new feature |
| `make brand-dry-run` | Preview all branding changes without applying |

---

## 📝 Roadmap

- [x] DataStore + Tink Secure Storage.
- [x] Navigation 3 Integration.
- [ ] Paging 3 Integration Guide.
- [ ] Automated Screen-shot Testing.

---

## 📄 License
Internal use only. See LICENSE for details.
