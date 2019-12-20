package ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.wimank.mvvm.weather.R
import com.wimank.mvvm.weather.databinding.FragmentCurrentlyWeatherBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import mvvm.binding.ObservableFields
import mvvm.viewmodel.CurrentlyWeatherViewModel
import org.jetbrains.anko.startActivity
import org.kodein.di.generic.instance
import ui.activity.SettingsActivity

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class CurrentlyWeatherFragment : KodeinFragment() {

    private val mViewModelFactory: ViewModelProvider.Factory by instance()

    private val mWeatherFields: ObservableFields by instance()

    private lateinit var mBinding: FragmentCurrentlyWeatherBinding

    private var mListener: CurrentlyWeatherFragmentListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_currently_weather,
            container,
            false
        )

        mBinding.viewModel = ViewModelProviders.of(this, mViewModelFactory)
            .get(CurrentlyWeatherViewModel::class.java)

        mBinding.observableFields = mWeatherFields

        mBinding.executePendingBindings()

        observer()

        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CurrentlyWeatherFragmentListener)
            mListener = context
        else
            throw RuntimeException("$context must implement CurrentlyWeatherFragmentListener")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_act_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                mListener?.showPlaceSearchFragment()
                return true
            }
            R.id.gps -> {
                mBinding.viewModel?.gps()
                return true
            }

            R.id.settings -> {
                context?.startActivity<SettingsActivity>()
                return true
            }
        }
        return false
    }

    private fun observer() {
        mWeatherFields.cancelPlaceSearch.observe(this, Observer {
            mListener?.hidePlacesFragment()
        })
        mWeatherFields.toolbarTitle.observe(this, Observer {
            mListener?.setTitle(it)
        })
    }

    /*  private fun hidePlacesFragment() {
          if (::placeAutoCompleteFragment.isInitialized) {
              fm.beginTransaction().hide(placeAutoCompleteFragment).commit()
              activity?.let { hideSoftInput(it) }
          }
      }*/

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface CurrentlyWeatherFragmentListener {
        fun showPlaceSearchFragment()
        fun hidePlacesFragment()
        fun setTitle(title: String)
    }

}
