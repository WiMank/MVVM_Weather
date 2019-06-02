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
import kotlinx.coroutines.ObsoleteCoroutinesApi
import mvvm.viewmodel.CurrentlyForecastViewModel
import org.kodein.di.generic.instance


@ObsoleteCoroutinesApi
class CurrentlyWeatherFragment : KodeinFragment() {
    private lateinit var binding: FragmentCurrentlyWeatherBinding
    private val viewModelFactory: ViewModelProvider.Factory by instance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_currently_weather, container, false)
        binding.viewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentlyForecastViewModel::class.java)
        binding.executePendingBindings()
        return binding.root
    }
}