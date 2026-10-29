package au.edu.jcu.assessment.utilityapp.data

import au.edu.jcu.assessment.utilityapp.model.AirQualitySnapshot
import au.edu.jcu.assessment.utilityapp.model.City
import au.edu.jcu.assessment.utilityapp.model.HourlyReading
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OutdoorRepository(
    private val api: AirQualityApi = Retrofit.Builder()
        .baseUrl("https://air-quality-api.open-meteo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AirQualityApi::class.java)
) {
    suspend fun fetch(city: City): AirQualitySnapshot {
        val response = api.getConditions(city.latitude, city.longitude)
        val current = response.current ?: throw IllegalStateException("Missing current conditions")
        val hourly = response.hourly
        val times = hourly?.time.orEmpty()
        return AirQualitySnapshot(
            updatedAt = current.time.orEmpty(),
            usAqi = current.usAqi?.takeIf { it >= 0 },
            europeanAqi = current.europeanAqi?.takeIf { it >= 0 },
            uvIndex = current.uvIndex?.takeIf { it.isFinite() && it >= 0 },
            pm25 = current.pm25?.takeIf { it.isFinite() && it >= 0 },
            pm10 = current.pm10?.takeIf { it.isFinite() && it >= 0 },
            hourly = times.mapIndexed { index, time ->
                HourlyReading(
                    time = time,
                    usAqi = hourly?.usAqi?.getOrNull(index)?.takeIf { it >= 0 },
                    europeanAqi = hourly?.europeanAqi?.getOrNull(index)?.takeIf { it >= 0 },
                    uvIndex = hourly?.uvIndex?.getOrNull(index)?.takeIf { it.isFinite() && it >= 0 }
                )
            }
        )
    }
}
