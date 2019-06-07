package mvvm.viewmodel

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import mvvm.model.dark_sky.ObservableFields
import mvvm.model.dark_sky.RepoDarkSkyForecast
import mvvm.model.status.Status
import mvvm.model.status.StatusChannel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class CurrentlyForecastViewModel(
    private val mRepoForecast: RepoDarkSkyForecast,
    private val handler: CoroutineExceptionHandler,
    private val observableFields: ObservableFields
) : ViewModel(), AnkoLogger {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)
    private val composite = CompositeDisposable()

    init {
        refresh()
    }


    fun refresh() {
        scope.launch(handler) {
            initStatusAndDb()
            //loadForecast()
        }
    }


    private suspend fun initStatusAndDb() {
        statusChannel()
        dataBaseObserve()
    }

    private suspend fun loadForecast() {
        observableFields.isLoading.set(true)
        mRepoForecast.loadGPSForecast()
        observableFields.isLoading.set(false)
    }


    private suspend fun loadPlaceNameForecast() {
        observableFields.isLoading.set(true)
        mRepoForecast.loadGPSForecast()
        observableFields.isLoading.set(false)
    }


    private suspend fun statusChannel() = scope.launch {
        StatusChannel.channel.consumeEach {
            when (it) {
                Status.LOCATION_DETERMINATION -> observableFields.status.set("Определяем местоположение...")
                Status.LOOKING_FOR_LOCATION_NAME -> observableFields.status.set("Пытаемся найти название местоположения...")
                Status.UPDATE_NEEDED -> observableFields.status.set("Проверяем актуальность данных...")
                Status.DATA_UP_TO_DATE -> observableFields.status.set("Данные в актуальном состоянии!")
                Status.SAVE_THE_DATA -> observableFields.status.set("Сохраняем новые данные...")
                Status.DONE -> observableFields.status.set("Готово!")
                Status.NO_NETWORK_CONNECTION -> observableFields.status.set("Нет подключения к сети!")
                Status.PLACE_COORDINATES -> observableFields.status.set("Узнаём координаты места...")
            }
        }
    }


    private suspend fun dataBaseObserve() {
        composite.add(
            mRepoForecast.db()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    observableFields.city.set(it.city)
                    observableFields.temp.set(it.temperature.toString())
                })
    }


    val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            info("$query")

            scope.launch {

                mRepoForecast.loadPlaceNameCoordinates(query ?: "")

                initStatusAndDb()
                if (observableFields.collapseSearchView.get())
                    observableFields.collapseSearchView.set(false)
                else
                    observableFields.collapseSearchView.set(true)
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            info("$newText")
            return false
        }
    }


    override fun onCleared() {
        super.onCleared()
        StatusChannel.channel.close()
        composite.dispose()
        job.cancel()
    }
}