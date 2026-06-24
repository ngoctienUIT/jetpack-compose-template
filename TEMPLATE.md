# Clone Checklist & Migration Guide

Follow this guide step-by-step when starting a new project from this template to ensure a seamless setup.

---

## Phase 1: Identity & Branding (The "First 5 Minutes")

1.  **Initialize**:
    ```bash
    make brand-init
    ```
2.  **Configure Identity**:
    - Open `config/brand.properties`.
    - Set `application_id` (e.g., `com.company.newapp`).
    - Set `app_name_production` and `app_name_staging`.
    - Set `theme_name` (this will be the name of your Compose theme class).
3.  **Visual Assets**:
    - Replace launcher icons in `brand/assets/icons/`.
    - Replace splash logo in `brand/assets/splash/logo.png`.
    - Set `primary_color` in `brand.properties`.
4.  **Apply Identity**:
    ```bash
    make brand-apply
    ```
    *Note: This will rename your packages, update AndroidManifest, and generate your Color palette.*

---

## Phase 2: Environment & Infrastructure

1.  **API Configuration**:
    - Edit `secrets/env.staging.properties` and `secrets/env.production.properties`.
    - Update `API_BASE_URL`.
2.  **Firebase Integration**:
    - Create projects in Firebase Console for both staging and production.
    - Download `google-services.json` and place them in `brand/assets/firebase/staging/` and `production/`.
    - Run `make brand-apply` to copy them to the app module.
3.  **Release Signing**:
    - Generate a release keystore.
    - Copy `secrets/signing.properties.example` to `secrets/signing.properties`.
    - Update keystore path and passwords (never commit this file).

---

## Phase 3: Core Logic Customization

1.  **Networking**:
    - Define your Retrofit interfaces in `core/network/api/`.
    - Provide them in `di/NetworkModule.kt`.
2.  **Authentication**:
    - Open `SplashScreen.kt`.
    - Replace the hardcoded navigation logic with your Auth check (Token check).
3.  **Deep Linking**:
    - Update `core/config/NotificationRoutes.kt` with your app's specific deep link paths.
4.  **Global UI Defaults**:
    - Adjust `ui/component/theme/AppComponentDefaults.kt` to match your brand's button shapes, image corners, etc.

---

## Phase 4: Feature Development

1.  **Create your first feature**:
    ```bash
    make gen-feature
    ```
2.  **Register Route**:
    - Add the new `@Serializable` route to `app/navigation/Route.kt`.
3.  **Add to Host**:
    - Register the screen in `app/navigation/AppNavHost.kt`.

---

## Phase 5: Production Checklist (Before Release)

- [ ] **R8 Verification**: Run `./gradlew :app:assembleProductionRelease` and verify the app opens without crashes.
- [ ] **Privacy Policy**: Review `AppInfoConfig` in `ConfigModule.kt`. Ensure you only send necessary device headers.
- [ ] **Security**: Ensure `base-config cleartextTrafficPermitted="false"` is set in `network_security_config.xml`.
- [ ] **Localization**: Verify all hardcoded strings are moved to `strings.xml` (En/Vi).
- [ ] **CI**: Push to GitHub/GitLab and ensure the CI pipeline passes.
