package mvvm.model

import android.content.SharedPreferences

class RepoPreference(private val sharedPreferences: SharedPreferences) {

    private val mPrefEditor = sharedPreferences.edit()

    fun <T> saveSettings(key: String, value: T) {
        when (value) {
            is String -> mPrefEditor.putString(key, value).apply()
            is Float -> mPrefEditor.putFloat(key, value).apply()
            is Boolean -> mPrefEditor.putBoolean(key, value).apply()
            is Int -> mPrefEditor.putInt(key, value).apply()
            is Long -> mPrefEditor.putLong(key, value).apply()
        }
    }

    fun getBooleanSettings(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getStringsSettings(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }
}
