package ui.fragment

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceSelectionListener
import com.wimank.mvvm.weather.R
import com.wimank.mvvm.weather.databinding.FragmentCurrentlyWeatherBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import mvvm.model.dark_sky.ObservableFields
import mvvm.viewmodel.CurrentlyForecastViewModel
import org.jetbrains.anko.support.v4.toast
import org.kodein.di.generic.instance
import secret.MAP_BOX_TOKEN


@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class CurrentlyWeatherFragment : KodeinFragment(), PlaceSelectionListener {

    override fun onCancel() {
    }


    override fun onPlaceSelected(carmenFeature: CarmenFeature?) {
        toast(carmenFeature?.text().toString())
    }


    private lateinit var binding: FragmentCurrentlyWeatherBinding
    private val viewModelFactory: ViewModelProvider.Factory by instance()
    private val observableFields: ObservableFields by instance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_currently_weather, container, false)
        binding.viewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentlyForecastViewModel::class.java)
        binding.observableFields = observableFields
        binding.executePendingBindings()
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_act_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.search -> {
                val options = PlaceOptions.builder()
                    .geocodingTypes(GeocodingCriteria.TYPE_PLACE, GeocodingCriteria.TYPE_REGION)
                    .build()

                val transaction = activity.let { it!!.supportFragmentManager.beginTransaction() }
                val placeAutoCompleteFragment = PlaceAutocompleteFragment.newInstance(MAP_BOX_TOKEN, options)
                placeAutoCompleteFragment!!.setOnPlaceSelectedListener(this)
                transaction.add(R.id.fragment_container, placeAutoCompleteFragment, PlaceAutocompleteFragment.TAG)
                transaction.commit()
                return true
            }
            else -> {
            }
        }

        return false
    }
}