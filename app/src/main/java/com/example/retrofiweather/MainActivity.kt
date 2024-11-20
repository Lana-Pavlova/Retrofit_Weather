package com.example.retrofiweather

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import android.widget.Button
import android.widget.EditText

interface WeatherApi {
    @GET("data/2.5/weather")
    fun getWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Call<WeatherData>
}

data class WeatherData(
    @SerializedName("name") val name: String,
    @SerializedName("main") val main: Main?
)

data class Main(
    @SerializedName("temp") val temp: Double
)
class MainActivity : AppCompatActivity() {
    private lateinit var cityEditText: EditText
    private lateinit var getWeatherButton: Button
    private lateinit var weatherTextView: TextView
    private lateinit var weatherApi: WeatherApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        cityEditText = findViewById(R.id.cityEditText)
        getWeatherButton = findViewById(R.id.getWeatherButton)
        weatherTextView = findViewById(R.id.weatherData)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        weatherApi = retrofit.create(WeatherApi::class.java)

        getWeatherButton.setOnClickListener {
            val cityName = cityEditText.text.toString()
            getWeatherForCity(cityName)
        }
    }

    private fun getWeatherForCity(cityName: String) {
        val apiKey = "45ea1dff5c7543b3d694cc968a8ad222"
        val call = weatherApi.getWeather(cityName, apiKey)
        call.enqueue(object : Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                if (response.isSuccessful) {
                    val weather = response.body()
                    runOnUiThread {
                        weatherTextView.text = "Город: ${weather?.name}, Температура: ${weather?.main?.temp} °C"
                    }
                } else {
                    runOnUiThread {
                        weatherTextView.text = "Ошибка: ${response.code()}"
                    }
                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                runOnUiThread {
                    weatherTextView.text = "Ошибка: ${t.message}"
                }
            }
        })
    }
}
//class MainActivity : AppCompatActivity() {
//    lateinit var textApi: TextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        supportActionBar?.hide()
//        textApi = findViewById(R.id.weatherData)
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://api.openweathermap.org/") // <--- Исправленный baseUrl
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val weatherApi = retrofit.create(WeatherApi::class.java)
//
//        val cityName = "Moscow"
//        val apiKey = "c4ad64c022ab2a95f6597884e6d937d0"
//
//        val call = weatherApi.getWeather(cityName, apiKey)
//        call.enqueue(object : Callback<WeatherData> {
//            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
//                if (response.isSuccessful) {
//                    val weather = response.body()
//                    runOnUiThread {
//                        textApi.text = "Город: ${weather?.name}, Температура: ${weather?.main?.temp} °C"
//                    }
//                } else {
//                    runOnUiThread {
//                        textApi.text = "Ошибка: ${response.code()}"
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
//                runOnUiThread {
//                    textApi.text = "Ошибка: ${t.message}"
//                }
//            }
//        })
//    }
//}