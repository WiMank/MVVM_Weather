package ui.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceSelectionListener
import com.wimank.mvvm.weather.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import mvvm.viewmodel.MainViewModel
import mvvm.viewmodel.MainViewModelFactory
import org.kodein.di.generic.instance
import secret.MAP_BOX_TOKEN
import ui.fragment.CurrentlyWeatherFragment
import ui.fragment.ErrorFragment


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class MainActivity : KodeinActivity(),
    CurrentlyWeatherFragment.CurrentlyWeatherFragmentListener,
    PlaceSelectionListener {

    private lateinit var mModel: MainViewModel

    private val mViewModelFactory: MainViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mModel = ViewModelProviders.of(this, mViewModelFactory).get(MainViewModel::class.java)

        if (savedInstanceState == null)
            showCurrentlyWeatherFragment()

        observable()
    }

    private fun observable() {
        mModel.mErrorLiveData.observe(this, Observer {
            supportFragmentManager.beginTransaction().run {
                if (it) {
                    ErrorFragment().apply {
                        replace(R.id.main_frame, this).commit()
                    }
                } else
                    showCurrentlyWeatherFragment()
            }
        })
    }

    override fun showPlaceSearchFragment() {
        val options = PlaceOptions.builder()
            .backgroundColor(ContextCompat.getColor(this, R.color.materialGray))
            .geocodingTypes(GeocodingCriteria.TYPE_PLACE, GeocodingCriteria.TYPE_REGION).build()

        supportFragmentManager.beginTransaction().run {
            PlaceAutocompleteFragment.newInstance(MAP_BOX_TOKEN, options).apply {
                setOnPlaceSelectedListener(this@MainActivity)
                replace(
                    R.id.main_frame,
                    this,
                    PlaceAutocompleteFragment.TAG
                ).commit()
            }
        }
    }

    override fun setActivityTitle(title: String) {
        setTitle(title)
    }

    override fun onCancel() {
        showCurrentlyWeatherFragment()
    }

    override fun onPlaceSelected(carmenFeature: CarmenFeature?) {
        mModel.savePlace(carmenFeature)
    }

    private fun showCurrentlyWeatherFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, CurrentlyWeatherFragment(), "frc")
            .commit()
    }

}
