package com.example.healthlog.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    const val BASE_URL = "https://healthlog-backend-u23h.onrender.com/"

    @Volatile
    var authToken: String? = null

    private val gson = GsonBuilder()
        .setLenient() 
        .create()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            authToken?.let { token ->
                if (token.isNotBlank()) {
                    requestBuilder.header("Authorization", "Bearer $token")
                }
            }
            chain.proceed(requestBuilder.build())
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()

    val instance: HealthLogApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        retrofit.create(HealthLogApi::class.java)
    }
}
