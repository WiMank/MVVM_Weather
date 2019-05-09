package mvvm.viewmodel

import androidx.lifecycle.ViewModel
import mvvm.model.RepoForecastModel
import org.kodein.di.Kodein
import org.kodein.di.generic.instance


class CurrentlyForecastViewModel(val kodein: Kodein) : ViewModel() {
    private val mRepoForecastModel: RepoForecastModel by kodein.instance()

    fun refresh() {
        mRepoForecastModel.getForecast()
    }
}