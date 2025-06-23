package com.example.weatherpredicitionapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val _forecastData = MutableStateFlow<List<DailyForecast>>(emptyList())
    val forecastData: StateFlow<List<DailyForecast>> = _forecastData

    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> = _weatherData

    private val weatherApi = WeatherAPI.create()

    fun fetchWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                // 🔍 Added this log
                println("🌍 Fetching weather for city: $city")

                val response = weatherApi.getWeather(city, apiKey)
                _weatherData.value = response

                // 🔍 Added this log
                println("✅ Current weather: ${response.main.temp}°C, ${response.weather[0].description}")

                // 🔍 Now fetch forecast (moved outside to show more clearly)
                fetch7DayForecast(response.coord.lat, response.coord.lon, apiKey)

            } catch (e: Exception) {
                // 🔍 More helpful log
                println("🔥 Error fetching weather: ${e.message}")
            }
        }
    }

    fun fetch7DayForecast(lat: Double, lon: Double, apiKey: String) {
        viewModelScope.launch {
            try {
                // 🔍 Added this log
                println("📅 Fetching forecast for coordinates: ($lat, $lon)")

                val forecast = weatherApi.getWeeklyForecast(lat, lon, apiKey = apiKey)

                // 🔍 Added this log to confirm how many days returned
                println("✅ Forecast received with ${forecast.daily.size} days")

                _forecastData.value = forecast.daily

            } catch (e: Exception) {
                // 🔍 More helpful log
                println("🔥 Error fetching forecast: ${e.message}")
            }
        }
    }
}


