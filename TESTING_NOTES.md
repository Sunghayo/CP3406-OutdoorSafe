# Local testing checklist (complete on your own device)

Record the date, device, and the actual result for each test. Don't mark untested items as passed.

| Test | Expected | Result / notes |
| --- | --- | --- |
| First launch with Internet | Today loads the selected city's AQI and UV or a clear unavailable state | |
| Refresh | Status updates without crashing | |
| Settings -> Seoul | Today switches to Seoul and fetches new data | |
| US -> European AQI | Label, number and category change to the selected scale | |
| Hide particle details | PM2.5/PM10 card disappears from Today | |
| Turn off Internet and refresh | Friendly error and Try again button; no fake AQI zero | |
| Reconnect and retry | Data loads again | |
| Scroll on Pixel 7 | Content is readable and does not overlap navigation bar | |
| Rotate / change font size | Verify basic usability if supported | |
| Run unit tests | Record actual Android Studio test result | |

## Reflection prompts

Write in your own words, using events you actually observed:

1. What made you choose an outdoor-condition monitoring utility?
2. How did you decide which metrics belong in the top summary?
3. What did you learn about fetching and mapping API data in Kotlin?
4. What bugs/errors did you encounter and how did you fix them?
5. How did you handle unavailable or potentially misleading environmental data?
6. What would you improve next?

Include citations and the university-required declaration for any use of GenAI or external materials.
