package ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.wimank.mvvm.weather.R
import com.wimank.mvvm.weather.databinding.FragmentCurrentlyWeatherBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import mvvm.model.dark_sky.ObservableFields
import mvvm.viewmodel.CurrentlyForecastViewModel
import org.kodein.di.generic.instance


@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class CurrentlyWeatherFragment : KodeinFragment() {
    private lateinit var binding: FragmentCurrentlyWeatherBinding
    private val viewModelFactory: ViewModelProvider.Factory by instance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_currently_weather, container, false)
        binding.viewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentlyForecastViewModel::class.java)
        binding.observableFields = ObservableFields()
        binding.executePendingBindings()
        return binding.root
    }
}