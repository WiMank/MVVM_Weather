package ui.fragment

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment
import com.wimank.mvvm.weather.R
import com.wimank.mvvm.weather.databinding.FragmentCurrentlyWeatherBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import mvvm.binding.ObservableFields
import mvvm.viewmodel.CurrentlyForecastViewModel
import org.jetbrains.anko.startActivity
import org.kodein.di.generic.instance
import secret.MAP_BOX_TOKEN
import ui.activity.SettingsActivity


@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class CurrentlyWeatherFragment : KodeinFragment() {
    private val viewModelFactory: ViewModelProvider.Factory by instance()
    private val observableFields: ObservableFields by instance()
    private lateinit var binding: FragmentCurrentlyWeatherBinding
    private lateinit var placeAutoCompleteFragment: PlaceAutocompleteFragment
    private lateinit var transaction: FragmentTransaction
    private val fm: FragmentManager by lazy { activity!!.supportFragmentManager }
    private val options by lazy {
        PlaceOptions.builder()
            .backgroundColor(ContextCompat.getColor(context!!, R.color.materialGray))
            .geocodingTypes(GeocodingCriteria.TYPE_PLACE, GeocodingCriteria.TYPE_REGION).build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_currently_weather, container, false)
        binding.viewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentlyForecastViewModel::class.java)
        binding.observableFields = observableFields
        binding.executePendingBindings()
        observer()
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
                showPlaceSearchFragment()
                return true
            }
            R.id.gps -> {
                binding.viewModel?.gps()
                return true
            }

            R.id.settings -> {
                context?.startActivity<SettingsActivity>()
                return true
            }
        }
        return false
    }

    private fun showPlaceSearchFragment() {
        if (::placeAutoCompleteFragment.isInitialized) {
            transaction = fm.beginTransaction()
            transaction.show(placeAutoCompleteFragment).commit()
        } else {
            transaction = fm.beginTransaction()
            placeAutoCompleteFragment = PlaceAutocompleteFragment.newInstance(MAP_BOX_TOKEN, options)
            placeAutoCompleteFragment.setOnPlaceSelectedListener(binding.viewModel)
            transaction.add(R.id.main_frame, placeAutoCompleteFragment, PlaceAutocompleteFragment.TAG)
            transaction.commit()
        }
    }

    private fun observer() {
        binding.viewModel.let {
            observableFields.cancelPlaceSearch.observe(this, Observer {
                hidePlacesFragment()
            })
        }

        binding.viewModel.let {
            observableFields.toolbarTitle.observe(this, Observer {
                activity!!.title = it
            })
        }
    }

    private fun hidePlacesFragment() {
        if (::placeAutoCompleteFragment.isInitialized) {
            fm.beginTransaction().hide(placeAutoCompleteFragment).commit()
            activity?.let { hideSoftInput(it) }
        }
    }
}