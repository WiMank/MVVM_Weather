package mvvm.binding

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class HourlyItem : BaseObservable() {
    @get: Bindable
    var time: Long = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.time)
        }

    @get: Bindable
    var icon: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.icon)
        }

    @get: Bindable
    var temp: Double = 0.0
        set(value) {
            field = value
            notifyPropertyChanged(BR.temp)
        }
}