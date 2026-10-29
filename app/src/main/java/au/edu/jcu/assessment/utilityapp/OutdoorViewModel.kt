package au.edu.jcu.assessment.utilityapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.jcu.assessment.utilityapp.data.OutdoorRepository
import au.edu.jcu.assessment.utilityapp.model.AirQualitySnapshot
import au.edu.jcu.assessment.utilityapp.model.AppSettings
import au.edu.jcu.assessment.utilityapp.model.AqiScale
import au.edu.jcu.assessment.utilityapp.model.City
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed interface OutdoorUiState {
    data object Loading : OutdoorUiState
    data class Success(val conditions: AirQualitySnapshot) : OutdoorUiState
    data class Error(val message: String) : OutdoorUiState
}

class OutdoorViewModel(private val repository: OutdoorRepository = OutdoorRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow<OutdoorUiState>(OutdoorUiState.Loading)
    val uiState: StateFlow<OutdoorUiState> = _uiState.asStateFlow()

    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    private var refreshJob: Job? = null

    init { refresh() }

    fun refresh() {
        refreshJob?.cancel()
        val city = _settings.value.city
        refreshJob = viewModelScope.launch {
            _uiState.value = OutdoorUiState.Loading
            try {
                _uiState.value = OutdoorUiState.Success(repository.fetch(city))
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: IOException) {
                _uiState.value = OutdoorUiState.Error("Could not connect. Check your internet and try again.")
            } catch (exception: HttpException) {
                _uiState.value = OutdoorUiState.Error("The air quality service returned an error (${exception.code()}). Try again.")
            } catch (exception: Exception) {
                _uiState.value = OutdoorUiState.Error("Conditions are unavailable right now. Please try again.")
            }
        }
    }

    fun setCity(city: City) {
        if (_settings.value.city == city) return
        _settings.update { it.copy(city = city) }
        refresh()
    }

    fun setAqiScale(scale: AqiScale) {
        _settings.update { it.copy(aqiScale = scale) }
    }

    fun setParticleDetails(visible: Boolean) {
        _settings.update { it.copy(showParticleDetails = visible) }
    }
}
