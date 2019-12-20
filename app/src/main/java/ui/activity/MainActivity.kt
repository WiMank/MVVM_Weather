package ui.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceSelectionListener
import com.wimank.mvvm.weather.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import secret.MAP_BOX_TOKEN
import ui.fragment.CurrentlyWeatherFragment

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class MainActivity : KodeinActivity(),
    CurrentlyWeatherFragment.CurrentlyWeatherFragmentListener,
    PlaceSelectionListener {

    private val options by lazy {
        PlaceOptions.builder()
            .backgroundColor(ContextCompat.getColor(this, R.color.materialGray))
            .geocodingTypes(GeocodingCriteria.TYPE_PLACE, GeocodingCriteria.TYPE_REGION).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.main_frame, CurrentlyWeatherFragment(), "frc")
                .commit()
        }
    }

    override fun showPlaceSearchFragment() {
        supportFragmentManager.beginTransaction().run {
            PlaceAutocompleteFragment.newInstance(MAP_BOX_TOKEN, options).apply {
                setOnPlaceSelectedListener(this@MainActivity)
                add(
                    R.id.main_frame,
                    this,
                    PlaceAutocompleteFragment.TAG
                ).commit()
            }
        }
    }

    override fun hidePlacesFragment() {

    }

    override fun setTitle(title: String) {

    }

    override fun onCancel() {

    }

    override fun onPlaceSelected(carmenFeature: CarmenFeature?) {

    }

}
