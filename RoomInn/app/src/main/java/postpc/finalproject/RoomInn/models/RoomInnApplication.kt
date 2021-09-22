package postpc.finalproject.RoomInn.models

import android.app.Application

class RoomInnApplication: Application() {

    // provide instance of the class to other classes in the app
    companion object {
        private lateinit var instance: RoomInnApplication
        @JvmStatic
        fun getInstance(): RoomInnApplication {
            return instance
        }
    }

    private lateinit var roomsDB: RoomsDB
    fun getRoomsDB(): RoomsDB {
        return roomsDB
    }

    override fun onCreate() {
        super.onCreate()

        //initialize:
        instance = this
        roomsDB = RoomsDB(this)
    }


}