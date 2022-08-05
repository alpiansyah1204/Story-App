package id.whynot.submission3.preference

import android.content.Context
import id.whynot.submission3.model.ModelUserPreferences

internal class Userprefreference(context: Context) {


    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun setUser(value: ModelUserPreferences) {
        val editor = preferences.edit()
        editor.putString(NAME, value.name)
        editor.putString(TOKEN, value.token)

        editor.apply()
    }

    fun getUser(): ModelUserPreferences {
        val model = ModelUserPreferences()
        model.name = preferences.getString(NAME, "")
        model.token = preferences.getString(TOKEN, "")

        return model
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val TOKEN = "token"

    }
}