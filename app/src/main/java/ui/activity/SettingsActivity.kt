package ui.activity

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.wimank.mvvm.weather.R
import mvvm.model.RepoPreference
import org.jetbrains.anko.AnkoLogger
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import utils.REPLACE_PREF

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_frame, SettingsFragment())
            .commit()
    }

    class SettingsFragment : PreferenceFragmentCompat(), KodeinAware,
        SharedPreferences.OnSharedPreferenceChangeListener,
        AnkoLogger {
        override val kodein by kodein()
        private val preference: RepoPreference by instance()


        override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
            preference.saveSettings(REPLACE_PREF, true)
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences
                .registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(this)
        }
    }
}
