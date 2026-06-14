# Clone checklist — follow in order when starting a new project from this template.

## 1. Identity
- [ ] Change `applicationId` and `namespace` in `app/build.gradle.kts`
- [ ] Refactor package `com.ngoctientnt.template` (Android Studio → Refactor → Rename)
- [ ] Update launcher icons in `res/mipmap-*`
- [ ] Update `app_name` in product flavor `resValue`

## 2. Environment & API
- [ ] Set `API_BASE_URL` per flavor in `app/build.gradle.kts`
- [ ] Add Retrofit API interfaces + `@Provides` in `di/NetworkModule.kt`
- [ ] Uncomment HTTP cleartext domains in `res/xml/network_security_config.xml` if needed for local dev

## 3. Firebase
- [ ] Create Firebase projects for staging and production
- [ ] Copy `google-services.json.example` → `google-services.json` per flavor folder
- [ ] Restrict API keys in Firebase/Google Cloud console

## 4. Signing & Release
- [ ] Copy `secrets/signing.properties.example` → `secrets/signing.properties`
- [ ] Generate release keystore and update paths/passwords
- [ ] Verify `./gradlew :app:assembleProductionRelease` succeeds with R8 enabled

## 5. Configuration (single place to customize)
- [ ] `di/ConfigModule.kt` — NetworkConfig, AppInfoConfig, AppImageLoaderConfig
- [ ] `core/config/AppConfig.kt` — splash delay, cache dir names
- [ ] `ui/theme/Color.kt` — brand colors
- [ ] `ui/component/theme/AppComponentDefaults.kt` — shared component styling

## 6. Privacy & Security
- [ ] Review `AppInfoConfig.includeAndroidId` / `includeUdidInHeaders` in `ConfigModule`
- [ ] Review `backup_rules.xml` exclusions for sensitive DataStore files
- [ ] Integrate crash reporting (Firebase Crashlytics / Sentry)

## 7. Features
- [ ] Wire auth in `SplashScreen` (replace hardcoded `MainRoute`)
- [ ] Send FCM token to backend in `FcmTokenManager.onNewToken`
- [ ] Add Room `@Database` when offline storage is needed
- [ ] Add Paging when implementing paginated lists

## 8. CI (recommended)
- [ ] Add GitHub Actions: compile, lint, unit tests
- [ ] Inject `secrets/signing.properties` and `google-services.json` from CI secrets
