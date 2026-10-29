package au.edu.jcu.assessment.utilityapp

import au.edu.jcu.assessment.utilityapp.model.AqiScale
import au.edu.jcu.assessment.utilityapp.ui.aqiLevel
import au.edu.jcu.assessment.utilityapp.ui.displayTime
import au.edu.jcu.assessment.utilityapp.ui.displayValue
import au.edu.jcu.assessment.utilityapp.ui.uvLevel
import org.junit.Assert.assertEquals
import org.junit.Test

class ConditionsFormatTest {
    @Test fun usAqiBoundaryValues() {
        assertEquals("Good", aqiLevel(50, AqiScale.US).title)
        assertEquals("Moderate", aqiLevel(51, AqiScale.US).title)
        assertEquals("Unhealthy for sensitive groups", aqiLevel(101, AqiScale.US).title)
        assertEquals("Unhealthy", aqiLevel(151, AqiScale.US).title)
    }

    @Test fun europeanScaleUsesDifferentThresholds() {
        assertEquals("Good", aqiLevel(20, AqiScale.EUROPEAN).title)
        assertEquals("Fair", aqiLevel(21, AqiScale.EUROPEAN).title)
        assertEquals("Poor", aqiLevel(61, AqiScale.EUROPEAN).title)
    }

    @Test fun uvIndexBandsAndMissingValues() {
        assertEquals("Low", uvLevel(2.9).title)
        assertEquals("Moderate", uvLevel(3.0).title)
        assertEquals("High", uvLevel(6.0).title)
        assertEquals("Extreme", uvLevel(11.0).title)
        assertEquals("Unavailable", uvLevel(null).title)
    }

    @Test fun missingDisplayValuesDoNotBecomeZero() {
        assertEquals("—", displayValue(null))
        assertEquals("—", displayValue(Double.NaN))
        assertEquals("13:00", displayTime("2026-10-29T13:00"))
    }
}
