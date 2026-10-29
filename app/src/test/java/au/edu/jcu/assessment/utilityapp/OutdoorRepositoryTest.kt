package au.edu.jcu.assessment.utilityapp

import au.edu.jcu.assessment.utilityapp.data.AirQualityApi
import au.edu.jcu.assessment.utilityapp.data.AirQualityResponse
import au.edu.jcu.assessment.utilityapp.data.CurrentConditionsDto
import au.edu.jcu.assessment.utilityapp.data.HourlyConditionsDto
import au.edu.jcu.assessment.utilityapp.data.OutdoorRepository
import au.edu.jcu.assessment.utilityapp.model.AqiScale
import au.edu.jcu.assessment.utilityapp.model.City
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OutdoorRepositoryTest {
    private fun repository(response: AirQualityResponse): OutdoorRepository = OutdoorRepository(object : AirQualityApi {
        override suspend fun getConditions(
            latitude: Double,
            longitude: Double,
            current: String,
            hourly: String,
            forecastHours: Int,
            timezone: String
        ): AirQualityResponse {
            assertEquals(City.SINGAPORE.latitude, latitude, 0.0)
            assertEquals(City.SINGAPORE.longitude, longitude, 0.0)
            assertEquals("auto", timezone)
            return response
        }
    })

    @Test fun mapsCurrentValuesAndHourlyReadings() = runBlocking {
        val snapshot = repository(AirQualityResponse(
            current = CurrentConditionsDto(time = "2026-10-29T12:00", usAqi = 56, europeanAqi = 28, uvIndex = 8.3, pm25 = 12.1),
            hourly = HourlyConditionsDto(time = listOf("2026-10-29T13:00", "2026-10-29T14:00"), usAqi = listOf(56, 60), uvIndex = listOf(7.2, 6.1))
        )).fetch(City.SINGAPORE)

        assertEquals(56, snapshot.aqi(AqiScale.US))
        assertEquals(28, snapshot.aqi(AqiScale.EUROPEAN))
        assertEquals(12.1, snapshot.pm25!!, 0.0)
        assertEquals(2, snapshot.hourly.size)
        assertEquals(60, snapshot.hourly[1].usAqi)
        assertNull(snapshot.hourly[1].europeanAqi)
    }

    @Test fun invalidOrAbsentFieldsRemainMissing() = runBlocking {
        val snapshot = repository(AirQualityResponse(
            current = CurrentConditionsDto(usAqi = -1, uvIndex = -2.0, pm25 = Double.NaN)
        )).fetch(City.SINGAPORE)
        assertNull(snapshot.usAqi)
        assertNull(snapshot.uvIndex)
        assertNull(snapshot.pm25)
        assertEquals(0, snapshot.hourly.size)
    }

    @Test(expected = IllegalStateException::class)
    fun missingCurrentSectionReportsError() {
        runBlocking {
            repository(AirQualityResponse(current = null)).fetch(City.SINGAPORE)
        }
    }
}
