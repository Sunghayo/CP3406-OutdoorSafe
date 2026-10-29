package au.edu.jcu.assessment.utilityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.jcu.assessment.utilityapp.ui.ConditionsScreen
import au.edu.jcu.assessment.utilityapp.ui.SettingsScreen
import au.edu.jcu.assessment.utilityapp.ui.theme.OutdoorSafeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { OutdoorSafeTheme { OutdoorSafeApp() } }
    }
}

@Composable
fun OutdoorSafeApp(outdoorViewModel: OutdoorViewModel = viewModel()) {
    var selectedTab by rememberSaveable { mutableStateOf("Today") }
    val state by outdoorViewModel.uiState.collectAsState()
    val settings by outdoorViewModel.settings.collectAsState()

    Scaffold(bottomBar = {
        NavigationBar {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                label = { Text("Today") },
                selected = selectedTab == "Today",
                onClick = { selectedTab = "Today" }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                label = { Text("Settings") },
                selected = selectedTab == "Settings",
                onClick = { selectedTab = "Settings" }
            )
        }
    }) { padding ->
        Box(Modifier.padding(padding)) {
            if (selectedTab == "Today") {
                ConditionsScreen(state, settings, outdoorViewModel::refresh)
            } else {
                SettingsScreen(
                    settings = settings,
                    onCityChange = outdoorViewModel::setCity,
                    onScaleChange = outdoorViewModel::setAqiScale,
                    onParticleDetailsChange = outdoorViewModel::setParticleDetails
                )
            }
        }
    }
}
