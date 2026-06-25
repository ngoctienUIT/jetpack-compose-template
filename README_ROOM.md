# Room Database Guide

Production-hardened Room setup for this template.

## Layout

```
core/database/
├── domain/
│   ├── model/User.kt
│   ├── repository/UserRepository.kt
│   └── usecase/
│       ├── ObserveUsersUseCase.kt
│       ├── ObserveUsersPagedUseCase.kt
│       ├── SaveUserUseCase.kt
│       └── DeleteUserUseCase.kt
└── data/
    ├── UserRepositoryImpl.kt
    ├── local/UserLocalDataSource.kt
    └── mapper/UserMapper.kt

data/local/
├── AppDatabase.kt
├── ManualMigrations.kt
├── dao/
├── entity/
└── security/DatabasePassphraseProvider.kt

di/
├── DatabaseModule.kt
└── UserRepositoryModule.kt
```

---

## 1. Add a New Entity

1. Create `data/local/entity/PostEntity.kt` with table/column constants and optional `@Index`.
2. Register in `AppDatabase.kt` and rebuild to export schema to `app/schemas/`.
3. Add domain model in `core/database/domain/model/` and mapper in `core/database/data/mapper/`.
4. Never expose entities to UI — map in the data layer.

---

## 2. Add a New DAO and Wire Through DI

1. Create `data/local/dao/PostDao.kt`.
2. Add `abstract fun postDao(): PostDao` on `AppDatabase`.
3. Add `@Provides` in `DatabaseModule`.
4. Create `PostLocalDataSource` in `core/database/data/local/` (mirror `UserLocalDataSource`).
5. Add repository interface + impl + Hilt `@Binds` module.

---

## 3. Migrations: AutoMigration First, Manual Fallback

**Simple changes** (nullable column, new table):

```kotlin
@Database(
    entities = [UserEntity::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [AutoMigration(from = 1, to = 2)],
)
```

**Complex changes** — add to `ManualMigrations.kt` and include in `ALL`.

| Build | Behavior |
|-------|----------|
| DEBUG | `fallbackToDestructiveMigration(dropAllTables = true)` |
| RELEASE | `@AutoMigration` + `ManualMigrations.ALL` |

Extend `AppDatabaseMigrationTest` when bumping version.

---

## 4. Security

### SQLCipher (RELEASE only)

- Native lib loaded once in `TemplateApplication` via `SqlCipherInitializer`
- Passphrase stored in encrypted DataStore + Tink (`DatabasePassphraseProvider`)
- `openHelperFactory(SupportOpenHelperFactory(...))` in `DatabaseModule` when `!BuildConfig.DEBUG`

### Backup exclusion

Keep in sync with [`SecureStorageNames.kt`](app/src/main/java/com/ngoctientnt/template/core/config/SecureStorageNames.kt):

- `database_passphrase_prefs.xml` (legacy migration source)
- `database_passphrase_keyset.xml` (Tink keyset)
- `app_database`, `app_database-shm`, `app_database-wal`

Configured in `backup_rules.xml` and `data_extraction_rules.xml`.

---

## 5. UseCases (recommended)

Inject UseCases in ViewModels, not repositories directly. This keeps the ViewModel clean and allows for easy reuse of business logic.

```kotlin
@HiltViewModel
class ProfileViewModel @Inject constructor(
    observeUsers: ObserveUsersUseCase,
    saveUser: SaveUserUseCase,
) : ViewModel() {
    // Collect Flow from UseCase
    val users = observeUsers().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}
```

- **Read operations**: Usually return `Flow<T>` or `Flow<List<T>>`.
- **Write operations**: Should return `Result<Unit>` to allow the UI to handle success/failure states.
- **Mapping**: The UseCase should typically return Domain models, while the Repository handles the conversion from Entities.

---

## 6. Paging (local Room sample)

```kotlin
// ViewModel
val usersPaged = observeUsersPagedUseCase()
    .cachedIn(viewModelScope)
```

Backed by `UserDao.pagingSource()` + `Pager` in `UserRepositoryImpl`.

---

## 7. Tests

### Unit tests (JVM, runs in CI)

```bash
./gradlew :app:testStagingDebugUnitTest
```

- `UserMapperTest` — mapping round-trip
- `UserRepositoryImplTest` — fake DAO + `Result` handling

### Instrumented tests (device/emulator required)

```bash
./gradlew :app:connectedStagingDebugAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=com.ngoctientnt.template.data.local.AppDatabaseTest
```

- `AppDatabaseTest` — DAO CRUD + `@Transaction replaceAll`
- `AppDatabaseMigrationTest` — `MigrationTestHelper` schema validation

### CI schema guard

CI runs `git diff --exit-code app/schemas/` after compile — commit schema JSON after every version bump.

---

## Key Conventions

| Topic | Convention |
|-------|------------|
| Database instance | Hilt `@Singleton` via `DatabaseModule` |
| Data access | Repository → LocalDataSource → DAO |
| Domain models | `core/database/domain/` only |
| Mapping | `core/database/data/mapper/` |
| SQLCipher | RELEASE builds only |
| Schema export | Commit `app/schemas/` after every bump |

See also: [`UserEntity.kt`](app/src/main/java/com/ngoctientnt/template/data/local/entity/UserEntity.kt), [`UserMapper.kt`](app/src/main/java/com/ngoctientnt/template/core/database/data/mapper/UserMapper.kt), [`DatabaseModule.kt`](app/src/main/java/com/ngoctientnt/template/di/DatabaseModule.kt).
