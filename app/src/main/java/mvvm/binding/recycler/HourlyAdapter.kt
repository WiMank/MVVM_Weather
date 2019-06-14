package mvvm.binding.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wimank.mvvm.weather.R
import com.wimank.mvvm.weather.databinding.HourlyRecyclerItemBinding
import mvvm.binding.HourlyItem
import mvvm.model.dark_sky.DarkSkyForecast
import mvvm.model.dark_sky.WeatherIcons

class HourlyAdapter(private val mData: DarkSkyForecast.Hourly) : RecyclerView.Adapter<HourlyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<HourlyRecyclerItemBinding>(
            inflater,
            R.layout.hourly_recycler_item,
            parent,
            false
        )
        return HourlyHolder(binding)
    }

    override fun getItemCount(): Int {
        return mData.data.size
    }

    override fun onBindViewHolder(holder: HourlyHolder, position: Int) {
        holder.bind(HourlyItem().apply {
            time = mData.data[position].time
            icon = WeatherIcons().map().getValue(mData.data[position].icon)
            day = mData.data[position].time
            temp = mData.data[position].temperature
        })
    }
}