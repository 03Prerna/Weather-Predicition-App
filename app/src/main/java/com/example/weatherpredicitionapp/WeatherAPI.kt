package com.example.weatherpredicitionapp
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"  // Use "imperial" for Fahrenheit
    ): WeatherResponse

    @GET("forecast")
    suspend fun getFiveDayForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): FiveDayForecastResponse



    companion object{
        private const val Base_URL ="https://api.openweathermap.org/data/2.5/"
        fun create():WeatherAPI{
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Base_URL)
                .build()
                return retrofit.create(WeatherAPI::class.java)
        }
    }
}