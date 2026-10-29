package au.edu.jcu.assessment.utilityapp.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface AirQualityApi {
    @GET("v1/air-quality")
    suspend fun getConditions(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "us_aqi,european_aqi,uv_index,pm2_5,pm10",
        @Query("hourly") hourly: String = "us_aqi,european_aqi,uv_index",
        @Query("forecast_hours") forecastHours: Int = 6,
        @Query("timezone") timezone: String = "auto"
    ): AirQualityResponse
}

data class AirQualityResponse(
    val current: CurrentConditionsDto? = null,
    val hourly: HourlyConditionsDto? = null
)

data class CurrentConditionsDto(
    val time: String? = null,
    @SerializedName("us_aqi") val usAqi: Int? = null,
    @SerializedName("european_aqi") val europeanAqi: Int? = null,
    @SerializedName("uv_index") val uvIndex: Double? = null,
    @SerializedName("pm2_5") val pm25: Double? = null,
    val pm10: Double? = null
)

data class HourlyConditionsDto(
    val time: List<String>? = null,
    @SerializedName("us_aqi") val usAqi: List<Int?>? = null,
    @SerializedName("european_aqi") val europeanAqi: List<Int?>? = null,
    @SerializedName("uv_index") val uvIndex: List<Double?>? = null
)
