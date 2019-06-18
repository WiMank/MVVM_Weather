package mvvm.model.status

import com.wimank.mvvm.weather.R

enum class Status {
    NO_NETWORK_CONNECTION, DATA_UP_TO_DATE, LOCATION_DETERMINATION, LOOKING_FOR_LOCATION_NAME, UPDATE_NEEDED, SAVE_THE_DATA, DONE, PLACE_COORDINATES
}

fun getStatusDescription(status: Status): Int {
    return when (status) {
        Status.LOCATION_DETERMINATION -> R.string.determine_the_location
        Status.LOOKING_FOR_LOCATION_NAME -> R.string.trying_loc
        Status.UPDATE_NEEDED -> R.string.check_relevance
        Status.SAVE_THE_DATA -> R.string.saving_new_data
        Status.PLACE_COORDINATES -> R.string.find_coordinates
        Status.NO_NETWORK_CONNECTION -> R.string.no_network_connection
        Status.DONE -> R.string.done
        Status.DATA_UP_TO_DATE -> R.string.data_up_to_date
    }
}
