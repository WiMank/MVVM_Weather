package mvvm.binding.recycler

import androidx.recyclerview.widget.RecyclerView
import com.wimank.mvvm.weather.databinding.HourlyRecyclerItemBinding
import mvvm.binding.HourlyItem

class HourlyHolder(private val binding: HourlyRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(hourlyItem: HourlyItem) {
        binding.hourlyItem = hourlyItem
        binding.executePendingBindings()
    }
}
