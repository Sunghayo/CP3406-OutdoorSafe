# OutdoorSafe – Daily Outdoor Conditions

OutdoorSafe is a small Android utility app for checking **air quality and UV exposure before going outside**. It is designed for a quick decision before a walk, commute or outdoor workout. Default location: Singapore.

Built from the CP3406 MobileTech Utility App teaching template. The original two-tab Compose navigation was retained and extended with a network-backed dashboard.

## Features

- **Today:** current US/European air quality index (AQI), UV index, simple status and outdoor activity guidance.
- Optional PM2.5 and PM10 concentration cards (µg/m³) and a short hourly forecast list.
- **Settings:** choose Singapore, Seoul, Sydney or London; choose US or European AQI scale; show/hide particle levels. Settings immediately control Today.
- Data fetched from the **Open-Meteo Air Quality API** with Retrofit; automatic loading, refresh and retry on connection failures.
- No login or GPS permission. The user explicitly selects a city. Settings last for the current app session, as allowed in Assessment 1.

The figures are **model-based estimates**, not readings from a local live sensor. The app is informational and does not promise personal safety. Both US and European AQI ratings use the corresponding scale (the numeric values are not interchangeable).

## How to run

1. Open the `OutdoorSafe` folder in Android Studio (not the ZIP file itself).
2. Allow Gradle Sync to complete. Internet is required to fetch dependencies and air-quality data.
3. Select an emulator (for example Pixel 7) and run the `app` configuration.
4. On Today, check the AQI/UV cards; open Settings to switch the city and AQI scale. Return to Today to see the updated data and labels.
5. Run local JVM tests through `app/src/test` or `./gradlew testDebugUnitTest`.

The Open-Meteo endpoint used is `https://air-quality-api.open-meteo.com/v1/air-quality`. It does **not** need an API key for non-commercial access under the provider's terms.

## Files and architecture

| File | Responsibility |
| --- | --- |
| `MainActivity.kt` | Two-tab navigation and Compose screen state |
| `OutdoorViewModel.kt` | StateFlow, city/scale/settings state, refresh and errors |
| `data/AirQualityApi.kt` | Retrofit endpoint and JSON response DTOs |
| `data/OutdoorRepository.kt` | Network data mapping, validation, hourly readings |
| `model/OutdoorModels.kt` | Domain models and available cities |
| `ui/ConditionsScreen.kt` | Main dashboard, metric cards, advice and hourly list |
| `ui/SettingsScreen.kt` | Location, AQI standard and particulate display controls |
| `ui/ConditionsFormat.kt` | Pure AQI and UV thresholds and display helpers |

## Tests / evidence to collect

- Unit tests cover AQI threshold categories, UV bands, missing data, and repository mapping with a fake service.
- **Student to verify:** Gradle Sync and emulator run, refresh, both AQI standards, switching cities, PM switch, offline retry.
- **Student to add:** screenshots of Today and Settings, Android Studio test results, and genuine Git commits describing actual changes.

## Constraints

- Internet required; no offline cache.
- Forecasts may be unavailable for some variables/regions; missing fields show `—` rather than an invented zero.
- City coordinates represent central urban locations, not device GPS.
- No location/background tracking, alert notifications or medical advice.

## Data attribution and sources

Data provided by **Open-Meteo** and **Copernicus Atmosphere Monitoring Service (CAMS)**, whose global model supplies the estimates. The provider requires attribution to Open-Meteo and CAMS; an in-app credit is also included.

- [Open-Meteo Air Quality API documentation](https://open-meteo.com/en/docs/air-quality-api)
- [Open-Meteo's API specification](https://github.com/open-meteo/open-meteo/blob/main/openapi/air-quality.yml)
- [Open-Meteo terms](https://open-meteo.com/en/terms)

Teaching starter: CP3406 MobileTech Utility App template. Sources and any GenAI assistance must also be recorded in the university's required declaration, per the updated assessment instructions. The student should write the self-reflection from **actual** decisions and testing experiences rather than a fabricated account.

## Assessment 1 alignment

| Criterion | Evidence |
| --- | --- |
| Code quality (20%) | Small, named files; distinct ViewModel/Repository/model/UI responsibilities |
| Purpose and UX (20%) | AQI + UV at a glance before outdoor activity, followed by relevant details |
| Android APIs (20%) | Internet permission, Retrofit HTTP API, error/loading states |
| Jetpack Compose (20%) | Material 3, two responsive screens, reusable cards, state-driven settings |
| GitHub (10%) | Add actual incremental commits and screenshots, keep README updated |
| Self-reflection (10%) | Reflect on actual implementation issues, technical learning and decisions |
