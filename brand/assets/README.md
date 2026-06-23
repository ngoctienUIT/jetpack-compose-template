# Brand assets

Drop replacement assets here, then run:

```bash
make brand-apply
# or individual steps:
./scripts/brand apply icons
./scripts/brand apply splash
./scripts/brand apply firebase
```

## icons/

| File | Description |
|------|-------------|
| `ic_launcher_foreground.xml` | Adaptive icon foreground (vector) |
| `ic_launcher_background.xml` | Adaptive icon background (vector) |
| `ic_stat_notification.xml` | FCM notification icon (optional, white glyph) |
| `ic_launcher_foreground.png` | 432×432 PNG — generates mipmap PNGs via `sips` (macOS) |

## splash/

| File | Description |
|------|-------------|
| `splash_logo.png` | In-app + system splash icon (recommended 432×432) |
| `splash_icon.png` | Alias filename also accepted |

Copied to `app/src/main/res/drawable/splash_icon.*`.

## firebase/

| Path | Description |
|------|-------------|
| `staging/google-services.json` | Firebase config for staging flavor |
| `production/google-services.json` | Firebase config for production flavor |

`package_name` must match `application_id` (+ staging suffix) from `config/brand.properties`.

See also `app/src/*/google-services.json.example` for placeholder structure.
