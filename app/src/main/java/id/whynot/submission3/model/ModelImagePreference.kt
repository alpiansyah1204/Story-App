package id.whynot.submission3.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModelImagePreference(
    var image1: String? = null,
    var image2: String? = null,
    var image3: String? = null,
    var image4: String? = null,
) : Parcelable


