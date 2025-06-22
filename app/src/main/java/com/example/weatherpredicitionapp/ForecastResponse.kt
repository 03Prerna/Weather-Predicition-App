package com.example.weatherpredicitionapp

data class ForecastResponse(
    val daily: List<DailyForecast>
)

data class DailyForecast(
    val dt: Long,
    val temp: Temp,
    val weather: List<Weather>
)

data class Temp(
    val day: Double
)

