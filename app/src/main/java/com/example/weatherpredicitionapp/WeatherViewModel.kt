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

                val response = weatherApi.getCurrentWeather(city, apiKey)
                _weatherData.value = response

                // 🔍 Added this log
                println("✅ Current weather: ${response.main.temp}°C, ${response.weather[0].description}")

                // 🔍 Now fetch forecast (moved outside to show more clearly)
                fetchFiveDayForecast(city, apiKey)


            } catch (e: Exception) {
                // 🔍 More helpful log
                println("🔥 Error fetching weather: ${e.message}")
            }
        }
    }

    fun fetchFiveDayForecast(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                println("📅 Fetching 5-day forecast for $city")

                val forecastResponse = weatherApi.getFiveDayForecast(city, apiKey)

                // Filter 12:00 PM entries for each day
                val dailyForecasts = forecastResponse.list.filter {
                    it.dt_txt.contains("12:00:00")
                }

                println("✅ Got ${dailyForecasts.size} entries at 12 PM")

                // Convert ForecastItem -> DailyForecast (reuse your existing UI model)
                val simplifiedForecast = dailyForecasts.map {
                    DailyForecast(
                        dt = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .parse(it.dt_txt)?.time?.div(1000) ?: 0L,
                        temp = Temp(it.main.temp),
                        weather = it.weather
                    )
                }

                _forecastData.value = simplifiedForecast

            } catch (e: Exception) {
                println("🔥 Error fetching 5-day forecast: ${e.message}")
            }
        }
    }
}



