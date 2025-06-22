package com.example.weatherpredicitionapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

 //   private val _forecast = MutableStateFlow<ForecastResponse?>(null)
 //   val forecast: StateFlow<ForecastResponse?> = _forecast
    private val _forecastData = MutableStateFlow<List<DailyForecast>>(emptyList())
    val forecastData: StateFlow<List<DailyForecast>> = _forecastData

    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> = _weatherData

    private val weatherApi= WeatherAPI.create()

    fun fetchWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(city, apiKey)
                _weatherData.value = response
                fetch7DayForecast(response.coord.lat, response.coord.lon, apiKey)
            } catch (e: Exception) {
                println("🔥 Error fetching weather: ${e.message}")
            }
        }
    }

    fun fetch7DayForecast(lat: Double, lon: Double, apiKey: String) {
        viewModelScope.launch {
            try {
                val forecast = weatherApi.getWeeklyForecast(lat, lon, apiKey = apiKey)
                _forecastData.value = forecast.daily
            } catch (e: Exception) {
                println("🔥 Error fetching forecast: ${e.message}")
            }
        }
    }


}

