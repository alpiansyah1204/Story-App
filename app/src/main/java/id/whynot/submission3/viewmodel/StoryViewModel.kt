package id.whynot.submission3.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.whynot.submission3.R
import id.whynot.submission3.model.ModelUserPreferences
import id.whynot.submission3.model.Post
import id.whynot.submission3.preference.Userprefreference
import id.whynot.submission3.request.RequestSignup
import id.whynot.submission3.request.Requestlogin
import id.whynot.submission3.response.PostResponse
import id.whynot.submission3.response.SignupResponse
import id.whynot.submission3.response.LoginResponse
import id.whynot.submission3.retrofitAndAPI.RetrofitClientStory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel : ViewModel() {
    private lateinit var userprefreference: Userprefreference
    val postItem = MutableLiveData<List<Post>>()
    val loginResponse = MutableLiveData<LoginResponse?>()
    val signupResponse = MutableLiveData<SignupResponse?>()
    fun setStoryMaps(context: Context, token: String) {
        RetrofitClientStory.apiInstance.getpostmaps(token).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                val listpost = arrayListOf<Post>()
                val postFromApi = response.body()
                if (postFromApi != null) {
                    Log.e("panjang : ", postFromApi.listStory?.size.toString())

                    for (posisition in postFromApi.listStory?.indices!!) {
                        val postingan = Post(
                            postFromApi.listStory!![posisition].name,
                            postFromApi.listStory!![posisition].description,
                            postFromApi.listStory!![posisition].photoUrl,
                            postFromApi.listStory!![posisition].lat,
                            postFromApi.listStory!![posisition].lon,
                        )
                        listpost.add(postingan)
                        Log.e("onResponse: suskes ", "$postingan")
                    }
                    postItem.postValue(listpost)
                    Log.e("onResponse: suskes ", "$postItem")
                }
                Toast.makeText(context, R.string.successgetdata, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Toast.makeText(context, R.string.failedgetdata, Toast.LENGTH_SHORT).show()
                Log.e("onFailure: ", t.message.toString())
            }

        })
    }

    fun getStoryMaps(): MutableLiveData<List<Post>> {
        return postItem
    }

    fun setLogin(context: Context, request: Requestlogin) {

        userprefreference = Userprefreference(context)
        RetrofitClientStory.apiInstance.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {


                val user = response.body()
                loginResponse.postValue(user)
                if (user != null) {
                    Log.e("onResponse: berhasil ", user.loginResult?.name.toString())
                    Log.e("onResponse: berhasil ", user.loginResult?.token.toString())
                    Toast.makeText(context, R.string.successlogin, Toast.LENGTH_SHORT)
                        .show()
                    val modeluser = ModelUserPreferences(
                        user.loginResult?.name.toString(),
                        user.loginResult?.token.toString()
                    )
                    userprefreference.setUser(modeluser)
                    Log.e("onResponse: ", modeluser.toString())
                    loginResponse.postValue(user)
                } else {

                    Log.e("onResponse: gagal ", user?.loginResult?.name.toString())
                    Log.e("onResponse: gagal ", user?.loginResult?.token.toString())
                    Toast.makeText(context, R.string.failedlogin2, Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                userprefreference.setUser(ModelUserPreferences("", ""))
                Log.e("onFailure:", "${t.message}")
                Toast.makeText(context, R.string.failedlogin, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getLogin(): MutableLiveData<LoginResponse?> {
        return loginResponse
    }

    fun setSignup(context: Context, request: RequestSignup) {

        userprefreference = Userprefreference(context)
        RetrofitClientStory.apiInstance.register(request)
            .enqueue(object : Callback<SignupResponse> {
                override fun onResponse(
                    call: Call<SignupResponse>,
                    response: Response<SignupResponse>
                ) {

                    val user = response.body()
                    signupResponse.postValue(user)
                    if (user != null) {
                        Log.e("onResponse: gagal ", user.error.toString())
                        Log.e("onResponse: berhasil ", user.message.toString())

                        Toast.makeText(context, R.string.successsignup, Toast.LENGTH_SHORT)
                            .show()

                    } else {

                        Toast.makeText(context, R.string.failedsignup2, Toast.LENGTH_SHORT)
                            .show()
                        Log.e("onResponse: gagaldong ", "${request.name}")
                        Log.e("onResponse: gagaldong ", "${request.email}")
                        Log.e("onResponse: gagaldong ", "${request.password}")

                    }

                }

                override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                    Log.e("onFailure:", "${t.message}")
                    Toast.makeText(context, R.string.failedsignup, Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    fun getSignup(): MutableLiveData<SignupResponse?> {
        return signupResponse
    }

}