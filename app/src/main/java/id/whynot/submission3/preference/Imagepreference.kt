package id.whynot.submission3.preference

import android.content.Context
import id.whynot.submission3.model.ModelImagePreference

internal class Imagepreference(context: Context) {

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun setUser(value: ModelImagePreference) {
        val editor = preferences.edit()
        editor.putString(IMAGE1, value.image1)
        editor.putString(IMAGE2, value.image2)
        editor.putString(IMAGE3, value.image3)
        editor.putString(IMAGE4, value.image4)

        editor.apply()
    }

    fun getUser(): ModelImagePreference {
        val model = ModelImagePreference()
        model.image1 = preferences.getString(IMAGE1, "")
        model.image2 = preferences.getString(IMAGE2, "")
        model.image3 = preferences.getString(IMAGE3, "")
        model.image4 = preferences.getString(IMAGE4, "")

        return model
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val IMAGE1 = "image1"
        private const val IMAGE2 = "image2"
        private const val IMAGE3 = "image3"
        private const val IMAGE4 = "image4"

    }
}