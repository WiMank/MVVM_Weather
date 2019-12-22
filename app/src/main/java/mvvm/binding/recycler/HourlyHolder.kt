package mvvm.binding.recycler

import androidx.recyclerview.widget.RecyclerView
import com.wimank.mvvm.weather.databinding.HourlyRecyclerItemBinding
import mvvm.binding.HourlyItem

class HourlyHolder(private val mBinding: HourlyRecyclerItemBinding) :
    RecyclerView.ViewHolder(mBinding.root) {
    fun bind(hourlyItem: HourlyItem) {
        mBinding.hourlyItem = hourlyItem
        mBinding.executePendingBindings()
    }
}
