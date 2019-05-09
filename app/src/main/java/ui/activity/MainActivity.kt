package ui.activity

import android.os.Bundle
import com.wimank.mvvm.weather.R
import ui.fragment.CurrentlyWeatherFragment

class MainActivity : KodeinActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.main_frame, CurrentlyWeatherFragment(), "text")
            .commit()

    }
}
