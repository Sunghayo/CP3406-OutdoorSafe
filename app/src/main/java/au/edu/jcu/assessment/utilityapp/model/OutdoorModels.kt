package au.edu.jcu.assessment.utilityapp.model

enum class City(val displayName: String, val country: String, val latitude: Double, val longitude: Double) {
    SINGAPORE("Singapore", "Singapore", 1.3521, 103.8198),
    SEOUL("Seoul", "South Korea", 37.5665, 126.9780),
    SYDNEY("Sydney", "Australia", -33.8688, 151.2093),
    LONDON("London", "United Kingdom", 51.5074, -0.1278)
}

enum class AqiScale { US, EUROPEAN }

data class AppSettings(
    val city: City = City.SINGAPORE,
    val aqiScale: AqiScale = AqiScale.US,
    val showParticleDetails: Boolean = true
)

data class HourlyReading(
    val time: String,
    val usAqi: Int?,
    val europeanAqi: Int?,
    val uvIndex: Double?
) {
    fun aqi(scale: AqiScale): Int? = if (scale == AqiScale.US) usAqi else europeanAqi
}

data class AirQualitySnapshot(
    val updatedAt: String,
    val usAqi: Int?,
    val europeanAqi: Int?,
    val uvIndex: Double?,
    val pm25: Double?,
    val pm10: Double?,
    val hourly: List<HourlyReading>
) {
    fun aqi(scale: AqiScale): Int? = if (scale == AqiScale.US) usAqi else europeanAqi
}
