package ui.recycler

import androidx.recyclerview.widget.RecyclerView
import com.wimank.mvvm.weather.databinding.HourlyRecyclerItemBinding
import mvvm.binding.HourlyItem

class HourlyHolder(private val binding: HourlyRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(hourlyItem: HourlyItem) {
        binding.apply {
            hourlyItemBinding = hourlyItem
            executePendingBindings()
        }
    }
}
