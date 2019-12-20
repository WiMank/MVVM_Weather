package ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.preference.PreferenceManager
import com.wimank.mvvm.weather.R
import org.jetbrains.anko.AnkoLogger
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

abstract class KodeinActivity : AppCompatActivity(), KodeinAware, AnkoLogger, LifecycleOwner {
    override val kodein by kodein()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false)
    }
}
