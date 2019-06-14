package mvvm.binding

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import mvvm.binding.recycler.HourlyAdapter
import mvvm.model.dark_sky.DarkSkyForecast

class ObservableFields {
    val temp = ObservableField<Double>(0.0)
    val summary = ObservableField<String>("SUMMARY")
    val status = ObservableField<String>("...")
    val statusInvisible = ObservableField<Boolean>(true)
    val weatherIcon = ObservableInt(0)
    val isLoading = ObservableBoolean(false)
    val cancelPlaceSearch = MutableLiveData<Boolean>()
    val toolbarTitle = MutableLiveData<String>("MVVM Weather")
    val hourlyAdapter: ObservableField<HourlyAdapter> = ObservableField(HourlyAdapter(DarkSkyForecast.Hourly()))
}