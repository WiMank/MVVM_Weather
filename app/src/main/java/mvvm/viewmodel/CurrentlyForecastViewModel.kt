package mvvm.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wimank.mvvm.weather.R
import mvvm.model.RepoForecast
import org.kodein.di.Kodein
import org.kodein.di.generic.instance


class CurrentlyForecastViewModel(val kodein: Kodein) : ViewModel() {

    private val mRepoForecast: RepoForecast by kodein.instance()

    var city = ObservableField<String>("CITY")
    var temp = ObservableField<String>("TEMP")
    var icon = ObservableField<Int>(R.drawable.ic_launcher_foreground)
    var isLoading = ObservableField<Boolean>(false)

    fun refresh() {
        mRepoForecast.forecast()
    }
}