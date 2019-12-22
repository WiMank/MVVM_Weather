package mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mvvm.model.RepoPreference

class MainViewModelFactory(private val mRepoPreference: RepoPreference) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(mRepoPreference) as T
    }
}
