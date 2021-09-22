package postpc.finalproject.RoomInn.models

import androidx.lifecycle.MutableLiveData
import postpc.finalproject.RoomInn.Room

data class User(
        var id: String = "not initialised id",
        var roomsList: MutableList<String> = mutableListOf() // string of the rooms names
) {
}