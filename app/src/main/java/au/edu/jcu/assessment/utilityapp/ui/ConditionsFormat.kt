package au.edu.jcu.assessment.utilityapp.ui

import au.edu.jcu.assessment.utilityapp.model.AqiScale
import java.util.Locale

data class Level(val title: String, val detail: String)

fun aqiLevel(value: Int?, scale: AqiScale): Level {
    if (value == null || value < 0) return Level("Unavailable", "No AQI estimate for this location.")
    return if (scale == AqiScale.US) when {
        value <= 50 -> Level("Good", "Air quality is generally satisfactory.")
        value <= 100 -> Level("Moderate", "Sensitive people may want to limit prolonged exertion.")
        value <= 150 -> Level("Unhealthy for sensitive groups", "Sensitive groups should reduce prolonged outdoor exertion.")
        value <= 200 -> Level("Unhealthy", "Consider reducing prolonged outdoor exertion.")
        value <= 300 -> Level("Very unhealthy", "Avoid prolonged outdoor exertion when possible.")
        else -> Level("Hazardous", "Avoid outdoor exertion when possible.")
    } else when {
        value <= 20 -> Level("Good", "Air quality is generally satisfactory.")
        value <= 40 -> Level("Fair", "Most people can continue normal outdoor activities.")
        value <= 60 -> Level("Moderate", "Sensitive people may want to limit prolonged exertion.")
        value <= 80 -> Level("Poor", "Consider reducing prolonged outdoor exertion.")
        value <= 100 -> Level("Very poor", "Avoid prolonged outdoor exertion when possible.")
        else -> Level("Extremely poor", "Avoid outdoor exertion when possible.")
    }
}

fun uvLevel(value: Double?): Level = when {
    value == null || !value.isFinite() || value < 0 -> Level("Unavailable", "No UV estimate for this location.")
    value < 3 -> Level("Low", "Sun protection may still be needed during extended exposure.")
    value < 6 -> Level("Moderate", "Consider shade and sun protection outdoors.")
    value < 8 -> Level("High", "Use shade, protective clothing and sunscreen.")
    value < 11 -> Level("Very high", "Limit midday sun exposure and use protection.")
    else -> Level("Extreme", "Minimise midday sun exposure and use protection.")
}

fun displayValue(value: Double?, decimals: Int = 1): String =
    value?.takeIf { it.isFinite() && it >= 0 }?.let { String.format(Locale.US, "%.${decimals}f", it) } ?: "—"

fun displayTime(iso: String): String =
    iso.substringAfter('T', "").take(5).ifBlank { "—" }
