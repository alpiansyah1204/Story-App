package id.whynot.submission3.retrofitAndAPI

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientStory {
    private const val BASE_URL = "https://story-api.dicoding.dev/v1/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiInstance: API = retrofit.create(API::class.java)
}