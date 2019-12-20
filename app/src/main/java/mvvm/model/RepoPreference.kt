package mvvm.model

import android.content.SharedPreferences

class RepoPreference(private val sharedPreferences: SharedPreferences) {

    private val prefEditor = sharedPreferences.edit()

    fun <T> saveSettings(key: String, value: T) {
        when (value) {
            is String -> prefEditor.putString(key, value).apply()
            is Float -> prefEditor.putFloat(key, value).apply()
            is Boolean -> prefEditor.putBoolean(key, value).apply()
            is Int -> prefEditor.putInt(key, value).apply()
            is Long -> prefEditor.putLong(key, value).apply()
        }
    }

    fun getBooleanSettings(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getStringsSettings(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }
}
