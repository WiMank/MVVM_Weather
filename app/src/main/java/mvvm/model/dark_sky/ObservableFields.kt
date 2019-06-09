package mvvm.model.dark_sky

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt

class ObservableFields {

    val city = ObservableField<String>("CITY")
    val temp = ObservableField<String>("TEMP")
    val summary = ObservableField<String>("SUMMARY")
    val status = ObservableField<String>("...")
    val icon = ObservableInt(0)
    val isLoading = ObservableBoolean(false)
    val collapseSearchView = ObservableBoolean(false)
    val gpsEnabled = ObservableBoolean(false)

}