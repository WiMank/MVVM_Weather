package ui.activity

import android.os.Bundle
import com.wimank.mvvm.weather.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import ui.fragment.CurrentlyWeatherFragment


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class MainActivity : KodeinActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fm = supportFragmentManager
        val fragment = fm.findFragmentById(R.id.main_frame)
        if (fragment == null) {
            fm.beginTransaction()
                .add(R.id.main_frame, CurrentlyWeatherFragment(), "frc")
                .commit()
        }
    }
}
