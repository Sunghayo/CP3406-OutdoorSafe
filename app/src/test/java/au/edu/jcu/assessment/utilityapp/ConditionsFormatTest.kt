package au.edu.jcu.assessment.utilityapp

import au.edu.jcu.assessment.utilityapp.model.AqiScale
import au.edu.jcu.assessment.utilityapp.ui.aqiLevel
import au.edu.jcu.assessment.utilityapp.ui.displayTime
import au.edu.jcu.assessment.utilityapp.ui.displayValue
import au.edu.jcu.assessment.utilityapp.ui.uvLevel
import au.edu.jcu.assessment.utilityapp.ui.Severity
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

    @Test fun aqiStatusesHaveDifferentSeverities() {
        assertEquals(Severity.GOOD, aqiLevel(30, AqiScale.US).severity)
        assertEquals(Severity.MODERATE, aqiLevel(83, AqiScale.US).severity)
        assertEquals(Severity.HIGH, aqiLevel(63, AqiScale.EUROPEAN).severity)
        assertEquals(Severity.VERY_HIGH, aqiLevel(95, AqiScale.EUROPEAN).severity)
        assertEquals(Severity.EXTREME, aqiLevel(105, AqiScale.EUROPEAN).severity)
        assertEquals(Severity.UNKNOWN, aqiLevel(null, AqiScale.US).severity)
    }

    @Test fun uvStatusesHaveDifferentSeverities() {
        assertEquals(Severity.GOOD, uvLevel(1.1).severity)
        assertEquals(Severity.MODERATE, uvLevel(3.0).severity)
        assertEquals(Severity.HIGH, uvLevel(7.0).severity)
        assertEquals(Severity.VERY_HIGH, uvLevel(8.0).severity)
        assertEquals(Severity.EXTREME, uvLevel(11.0).severity)
        assertEquals(Severity.UNKNOWN, uvLevel(Double.NaN).severity)
    }

    @Test fun missingDisplayValuesDoNotBecomeZero() {
        assertEquals("—", displayValue(null))
        assertEquals("—", displayValue(Double.NaN))
        assertEquals("13:00", displayTime("2026-10-29T13:00"))
    }
}
