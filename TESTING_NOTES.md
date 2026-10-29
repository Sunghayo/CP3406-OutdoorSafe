# OutdoorSafe — Testing Notes

**Test date:** 29 October 2026  
**Environment:** Android Studio; Pixel 7 Android emulator (API 37); macOS  
**Command used:** `bash gradlew :app:testDebugUnitTest`

| Check | Actual observation | Status |
| --- | --- | --- |
| Launch Today online | Singapore AQI/UV appeared and the forecast model notice was visible | Passed |
| Settings navigation | Today and Settings navigation worked | Passed |
| Change location | Singapore → Seoul updated city/country and conditions | Passed |
| AQI scale | US → European updated index and category | Passed |
| Particle toggle | Turning off details removed PM2.5/PM10 section | Passed |
| UI severity | Moderate AQI shown yellow; Low UV shown green | Passed |
| Offline | Disabled network and refreshed; error message and Try again appeared without app closing | Passed |
| Recovery | Re-enabled network and tapped Try again; dashboard data returned | Passed |
| Local unit tests | Gradle task ended with `BUILD SUCCESSFUL in 4s` | Passed |
| Other screen sizes | Not yet tested | Not tested |
| Dark mode | Palette defined in code; manual screenshot not supplied | Not tested |
| Screen rotation / large font | Not yet tested | Not tested |

## Issue encountered during testing

The first unit-test attempt failed with `OutdoorRepositoryTest > initializationError`. A change to the test's `runBlocking` function resulted in a missing `}` compiler error. After correcting the test function and final brace, the Gradle unit test task succeeded.

There was also a source-copy issue when transferring a UI improvement into the Git clone: many existing source/resource files appeared as deleted. Restoring only missing tracked files with `git ls-files -d -z | xargs -0 git restore --worktree --` left the intended UI changes intact.

## Evidence

- `docs/screenshots/today.png` — working Today screen after the colour change
- `docs/screenshots/settings.png` — Settings controls
- `docs/screenshots/offline-error.png` — disconnected state and retry button

Screenshots support the visible UI behaviour. Details such as the exact live readings and the number of unit tests can change between runs; do not present screen values as universally fixed.
