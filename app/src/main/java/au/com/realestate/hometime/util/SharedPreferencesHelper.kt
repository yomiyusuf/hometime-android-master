package au.com.realestate.hometime.util

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

private const val PREF_API_TOKEN = "api_token"

class SharedPreferencesHelper(context: Context) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    var apiToken: String?
        get() {
            return prefs.getString(PREF_API_TOKEN, null)
        }
        set(value) {
            prefs.edit { putString(PREF_API_TOKEN, value) }
        }
}