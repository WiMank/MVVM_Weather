package mvvm.binding

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wimank.mvvm.weather.BR

class CurrentlyForecastBinding : BaseObservable() {

    @get:Bindable
    var city: String = "..."
        set(value) {
            field = value
            notifyPropertyChanged(BR.city)
        }

    @get:Bindable
    var temp: String = "0"
        set(value) {
            field = value
            notifyPropertyChanged(BR.temp)
        }

    @get:Bindable
    var icon: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.icon)
        }
}