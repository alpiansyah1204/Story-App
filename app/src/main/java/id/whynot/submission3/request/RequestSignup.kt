package id.whynot.submission3.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestSignup {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null
}