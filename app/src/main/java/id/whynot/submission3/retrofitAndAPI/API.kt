package id.whynot.submission3.retrofitAndAPI

import id.whynot.submission3.request.FileUploadResponse
import id.whynot.submission3.request.Requestlogin
import id.whynot.submission3.request.RequestSignup
import id.whynot.submission3.response.PostResponse
import id.whynot.submission3.response.LoginResponse
import id.whynot.submission3.response.SignupResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface API {

    @POST("login")
    @Headers("Authorization: token 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")
    fun login(
        @Body userRequest: Requestlogin
    ): Call<LoginResponse>

    @POST("register")
    @Headers("Authorization: token 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")
    fun register(
        @Body userRequest: RequestSignup
    ): Call<SignupResponse>


    @Multipart
    @POST("/v1/stories")
    fun uploadImage(
        @Header("AUTHORIZATION") value: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<FileUploadResponse>

    @Multipart
    @POST("/v1/stories")
    fun uploadImagewithlocation(
        @Header("AUTHORIZATION") value: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double,
        @Part("lon") lon: Double
    ): Call<FileUploadResponse>

    @GET("stories")
    fun getpostmaps(
        @Header("AUTHORIZATION") value: String,
        @Query("location") location: Int = 1,
    ): Call<PostResponse>
}