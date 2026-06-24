# Android Production Template

A high-performance, production-ready Android application template built with **Kotlin**, **Jetpack Compose**, and **Material Design 3**. This project is designed as a robust foundational layer for senior developers to bootstrap new projects in minutes, not days.

---

## 🎯 Project Philosophy

- **Zero-Boilerplate**: Standardized Base classes and automated code generation.
- **Production Hardened**: Pre-configured R8/Proguard rules, secure networking, and privacy-first defaults.
- **Highly Scalable**: Feature-first package structure that works for small and large-scale apps.
- **Automation Driven**: One-command rebranding, identity management, and CI/CD ready.

---

## 🛠 Tech Stack & Architecture

### Core Architecture
- **Architecture**: MVI (Model-View-Intent) using a custom `BaseViewModel`.
- **UI Framework**: Jetpack Compose with Material 3.
- **Dependency Injection**: Hilt 2.59.
- **Navigation**: Navigation 3 + Kotlin Serialization (Type-safe).
- **Concurrency**: Kotlin Coroutines & Flow.

### Networking & Data
- **Networking**: Retrofit 3 + OkHttp 5 + Kotlinx Serialization.
- **Local Storage**: DataStore Preferences + Room (ready-to-use).
- **Image Loading**: Coil 2.7 (shared OkHttp client).

### Platform & DevEx
- **Firebase**: Analytics, Messaging (FCM), Remote Config, Performance.
- **Connectivity**: Real-time network monitoring with global UI state.
- **Localization**: Per-app language support (En/Vi) via AppCompat.
- **Monitoring**: AppLogger (debug-only) + pre-configured Proguard line numbers.

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
1.  Edit `config/brand.properties` to set your Package Name, App Name, and Brand Colors.
2.  Add your assets (icons, splash logo) to `brand/assets/`.
3.  Run the rebranding pipeline:
    ```bash
    make brand-apply
    ```

### 4. API & Firebase
1.  Set your API URLs in `secrets/env.staging.properties` and `secrets/env.production.properties`.
2.  Place your `google-services.json` in `brand/assets/firebase/{staging,production}/`.
3.  Run `make brand-apply` again to sync Firebase configs.

---

## 🏗 Architecture & Patterns

### BaseViewModel (MVI)
All new features should extend `BaseViewModel<State, Intent, Effect>`. This reduces boilerplate and ensures a consistent unidirectional data flow.

- **State**: Persistent UI state.
- **Intent**: User actions (e.g., `OnLoginClicked`).
- **Effect**: One-time side effects (e.g., `ShowToast`, `Navigate`).

### Feature Scaffolding
Create a complete feature (Contract, ViewModel, Screen) in seconds:
```bash
make gen-feature
```

---

## 📦 Project Structure

```
Template/
├── app/                        # Main Android module
│   ├── src/main/java/.../
│   │   ├── app/                # Root navigation & DI setup
│   │   ├── core/               # Shared business logic & infrastructure
│   │   ├── di/                 # Hilt modules (ConfigModule is the entry point)
│   │   ├── feature/            # Feature-based screens (MVI)
│   │   └── ui/                 # Design system & shared components
├── scripts/                    # Automation scripts (Python/Bash)
├── config/                     # Brand & Product configuration
├── secrets/                    # Environment variables (git-ignored)
└── brand/                      # Raw brand assets (icons, splash)
```

---

## 🔐 Production Readiness

- **R8/Proguard**: Comprehensive rules for Coroutines, Serialization, and Retrofit are pre-configured in `app/proguard-rules.pro`.
- **Security**: `network_security_config.xml` enforces HTTPS. Sensitive DataStore files are excluded from Android Auto-backup.
- **Signing**: Pre-configured template for release signing in `secrets/signing.properties`.
- **CI/CD**: GitHub Actions workflow included for automated testing and APK building.

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

- [ ] Room Database Implementation Example.
- [ ] Paging 3 Integration Guide.
- [ ] Automated Screen-shot Testing.
- [ ] Dark Mode Preview Gallery.

---

## 👥 Contributing
Senior developers are encouraged to contribute to the `core/` modules to improve the foundation for all projects.

---

## 📄 License
Internal use only. See LICENSE for details.
