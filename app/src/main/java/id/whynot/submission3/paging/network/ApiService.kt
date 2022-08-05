package id.whynot.submission3.paging.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("stories")
    suspend fun getQuote(
        @Header("AUTHORIZATION") value: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ResponseStory
}