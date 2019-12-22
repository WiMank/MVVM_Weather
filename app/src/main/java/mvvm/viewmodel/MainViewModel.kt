package mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import mvvm.model.RepoPreference
import utils.GPS_KEY
import utils.PLACE_KEY
import utils.SEARCH_QUERY

class MainViewModel(private val mPreference: RepoPreference) : ViewModel() {

    val mErrorLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun savePlace(carmenFeature: CarmenFeature?) {
        if (carmenFeature == null) {
            mErrorLiveData.value = true
            return
        }
        if (carmenFeature.center() != null) {
            mPreference.saveSettings(PLACE_KEY, true)
            mPreference.saveSettings(GPS_KEY, false)
            mPreference.saveSettings(SEARCH_QUERY, carmenFeature.text() ?: "")
            mErrorLiveData.value = false
        } else
            mErrorLiveData.value = true
    }

}
