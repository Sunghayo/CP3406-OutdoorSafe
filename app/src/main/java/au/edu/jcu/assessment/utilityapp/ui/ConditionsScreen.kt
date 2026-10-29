package au.edu.jcu.assessment.utilityapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import au.edu.jcu.assessment.utilityapp.OutdoorUiState
import au.edu.jcu.assessment.utilityapp.model.AirQualitySnapshot
import au.edu.jcu.assessment.utilityapp.model.AppSettings
import au.edu.jcu.assessment.utilityapp.model.AqiScale
import au.edu.jcu.assessment.utilityapp.model.HourlyReading
import au.edu.jcu.assessment.utilityapp.ui.theme.OutdoorSafeTheme

@Composable
fun ConditionsScreen(state: OutdoorUiState, settings: AppSettings, onRefresh: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 12.dp)) {
        ConditionsHeader(onRefresh)
        Spacer(Modifier.height(12.dp))
        when (state) {
            OutdoorUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(12.dp))
                    Text("Checking outdoor conditions…")
                }
            }
            is OutdoorUiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(state.message, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = onRefresh) { Text("Try again") }
                }
            }
            is OutdoorUiState.Success -> ConditionsContent(state.conditions, settings)
        }
    }
}

@Composable
private fun ConditionsHeader(onRefresh: () -> Unit) {
    val largeText = LocalDensity.current.fontScale >= 1.3f
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val availableWidth = this.maxWidth
        val stackedHeader = largeText || availableWidth < 340.dp
        if (stackedHeader) {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("OutdoorSafe", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Before you head outside", color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedButton(onClick = onRefresh, modifier = Modifier.align(Alignment.End)) {
                    Text("Refresh")
                }
            }
        } else {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("OutdoorSafe", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("Before you head outside", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                OutlinedButton(onClick = onRefresh) { Text("Refresh") }
            }
        }
    }
}

@Composable
private fun ConditionsContent(data: AirQualitySnapshot, settings: AppSettings) {
    val aqi = data.aqi(settings.aqiScale)
    val aqiStatus = aqiLevel(aqi, settings.aqiScale)
    val uvStatus = uvLevel(data.uvIndex)
    LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(settings.city.displayName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Text(settings.city.country, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text("Local ${displayTime(data.updatedAt)}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        item {
            Card(
                Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("OUTDOOR CONDITIONS AT A GLANCE", style = MaterialTheme.typography.labelMedium)
                    BoxWithConstraints(Modifier.fillMaxWidth()) {
                        val availableWidth = this.maxWidth
                        val stackedMetrics = availableWidth < 300.dp || LocalDensity.current.fontScale >= 1.3f
                        if (stackedMetrics) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                MetricBlock(
                                    modifier = Modifier.fillMaxWidth(),
                                    label = if (settings.aqiScale == AqiScale.US) "US AIR QUALITY" else "EUROPEAN AQI",
                                    value = aqi?.toString() ?: "—",
                                    level = aqiStatus
                                )
                                MetricBlock(
                                    modifier = Modifier.fillMaxWidth(),
                                    label = "UV INDEX",
                                    value = displayValue(data.uvIndex),
                                    level = uvStatus
                                )
                            }
                        } else {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                MetricBlock(
                                    modifier = Modifier.weight(1f),
                                    label = if (settings.aqiScale == AqiScale.US) "US AIR QUALITY" else "EUROPEAN AQI",
                                    value = aqi?.toString() ?: "—",
                                    level = aqiStatus
                                )
                                MetricBlock(
                                    modifier = Modifier.weight(1f),
                                    label = "UV INDEX",
                                    value = displayValue(data.uvIndex),
                                    level = uvStatus
                                )
                            }
                        }
                    }
                    HorizontalDivider()
                    Text("Forecast model estimates, not live sensor readings.", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        item {
            Text("Plan your outdoor time", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        }
        item { AdviceCard("Air quality", aqiStatus) }
        item { AdviceCard("Sun exposure", uvStatus) }
        if (settings.showParticleDetails) {
            item {
                Text("Particle levels", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            }
            item {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        ParticleRow("PM2.5", data.pm25)
                        HorizontalDivider()
                        ParticleRow("PM10", data.pm10)
                        Text("Concentration in µg/m³", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
        if (data.hourly.isNotEmpty()) {
            item { Text("Next hours", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold) }
            items(data.hourly, key = { it.time }) { reading -> HourRow(reading, settings) }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Data: Open-Meteo · Copernicus CAMS ENSEMBLE", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("UV and air quality estimates are guidance, not a guarantee of safety.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun MetricBlock(modifier: Modifier, label: String, value: String, level: Level) {
    val colors = statusColors(level.severity)
    Surface(modifier = modifier, shape = MaterialTheme.shapes.medium, color = colors.background) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = colors.foreground)
            Text(value, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = colors.foreground)
            Text(level.title, style = MaterialTheme.typography.labelMedium, color = colors.foreground)
        }
    }
}

@Composable
private fun AdviceCard(title: String, level: Level) {
    val colors = statusColors(level.severity)
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(title, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                Surface(shape = MaterialTheme.shapes.small, color = colors.background) {
                    Text(
                        level.title,
                        modifier = Modifier.widthIn(max = 180.dp).padding(horizontal = 9.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.foreground,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Text(level.detail, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun ParticleRow(name: String, value: Double?) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(name, style = MaterialTheme.typography.bodyMedium)
        Text(displayValue(value), fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun HourRow(reading: HourlyReading, settings: AppSettings) {
    Card(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(displayTime(reading.time), modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
            Column(horizontalAlignment = Alignment.End) {
                Text("AQI ${reading.aqi(settings.aqiScale) ?: "—"}", style = MaterialTheme.typography.bodyMedium)
                Text("UV ${displayValue(reading.uvIndex)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConditionsPreview() {
    OutdoorSafeTheme {
        ConditionsScreen(
            state = OutdoorUiState.Success(AirQualitySnapshot(
                updatedAt = "2026-10-29T14:00", usAqi = 52, europeanAqi = 29, uvIndex = 6.4,
                pm25 = 11.5, pm10 = 23.2, hourly = listOf(HourlyReading("2026-10-29T15:00", 55, 30, 4.5))
            )),
            settings = AppSettings(),
            onRefresh = {}
        )
    }
}
