package mvvm.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wimank.mvvm.weather.R
import kotlinx.coroutines.*
import mvvm.model.RepoForecastModel
import org.kodein.di.Kodein
import org.kodein.di.generic.instance


class CurrentlyForecastViewModel(val kodein: Kodein) : ViewModel() {
    private val mRepoForecastModel: RepoForecastModel by kodein.instance()

    var city = ObservableField<String>("CITY")
    var temp = ObservableField<String>("TEMP")
    var icon = ObservableField(0)
    var isLoading = ObservableField<Boolean>(false)

    fun refresh() {
        isLoading.set(true)
        GlobalScope.launch {
            delay(3000)
            withContext(Dispatchers.Main) {
                city.set("New data")
                temp.set("New temp")
                icon.set(R.drawable.ic_cloud_sun_solid)
                isLoading.set(false)
            }
        }
    }
}