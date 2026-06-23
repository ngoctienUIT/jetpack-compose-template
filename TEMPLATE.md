# Clone checklist — follow in order when starting a new project from this template.

## 1. Brand & identity (automated)

```bash
make brand-init
# Edit config/brand.properties (application_id, app names, colors, theme)
# Add assets under brand/assets/ (icons, splash, firebase) — see brand/assets/README.md
make brand-dry-run    # preview changes
make brand-apply      # apply identity, colors, icons, splash, firebase
make brand-validate   # verify Firebase package names and config
```

Manual follow-ups after `brand apply`:
- [ ] Review `config/brand.properties` — set `legacy.*` to template values before first apply
- [ ] Sync `ui/theme/Color.kt` Compose palette if needed (XML colors applied automatically)
- [ ] Run `./gradlew :app:assembleStagingDebug` and smoke-test splash + launcher icon

Gradle also exposes:
- `./gradlew :app:brandValidate`
- `./gradlew :app:brandApplyIdentity -PbrandDryRun=true`

## 2. Environment & API
- [ ] Copy `secrets/env.staging.properties.example` → `secrets/env.staging.properties` (or use `make brand-init`)
- [ ] Copy `secrets/env.production.properties.example` → `secrets/env.production.properties`
- [ ] Set `API_BASE_URL`, OAuth IDs per flavor
- [ ] Add Retrofit API interfaces + `@Provides` in `di/NetworkModule.kt`
- [ ] Uncomment HTTP cleartext domains in `res/xml/network_security_config.xml` if needed for local dev

## 3. Firebase
- [ ] Create Firebase projects for staging and production
- [ ] Place configs in `brand/assets/firebase/{staging,production}/google-services.json` and run `make brand-apply`
- [ ] Or copy `app/src/*/google-services.json.example` → `google-services.json` per flavor folder
- [ ] Restrict API keys in Firebase/Google Cloud console

## 4. Signing & Release
- [ ] Copy `secrets/signing.properties.example` → `secrets/signing.properties`
- [ ] Generate release keystore and update paths/passwords
- [ ] Verify `./gradlew :app:assembleProductionRelease` succeeds with R8 enabled

## 5. Configuration (single place to customize)
- [ ] `config/brand.properties` — app id, names, colors, splash delay, cache dir name
- [ ] `di/ConfigModule.kt` — NetworkConfig, AppInfoConfig, AppImageLoaderConfig
- [ ] `ui/theme/Color.kt` — Compose brand colors
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
