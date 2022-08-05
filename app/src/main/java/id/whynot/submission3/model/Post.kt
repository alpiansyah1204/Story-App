package id.whynot.submission3.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Post(
    var name: String? = null,
    var description: String? = null,
    var photoUrl: String? = null,
    var lat: Double? = null,
    var lon: Double? = null,
) : Parcelable