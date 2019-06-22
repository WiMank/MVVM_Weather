package mvvm.binding

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.wimank.mvvm.weather.R
import mvvm.binding.recycler.HourlyAdapter
import mvvm.model.dark_sky.DarkSkyForecast

class ObservableFields {
    val temp = ObservableField(0.0)
    val summary = ObservableField("SUMMARY")
    val status = ObservableField(R.string.empty)
    val statusInvisible = ObservableField(true)
    val weatherIcon = ObservableInt(0)
    val isLoading = ObservableBoolean(false)
    val cancelPlaceSearch = MutableLiveData<Boolean>()
    val toolbarTitle = MutableLiveData("MVVM Weather")
    val hourlyAdapter: ObservableField<HourlyAdapter> = ObservableField(HourlyAdapter(DarkSkyForecast.Hourly()))
}