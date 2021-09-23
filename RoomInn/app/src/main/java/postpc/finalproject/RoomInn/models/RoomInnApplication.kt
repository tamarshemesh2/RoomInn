package postpc.finalproject.RoomInn.models

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import postpc.finalproject.RoomInn.furnitureData.Point3D
import java.io.BufferedReader
import java.io.File


class RoomInnApplication: Application() {
    var pathToUnity = ""
    val json = Gson()

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
    fun readFromFileToPoints(f: String): MutableList<Point3D> {
        //Read the file
        val bufferedReader: BufferedReader = File(pathToUnity+f).bufferedReader()
        // Read the text from bufferReader and store in String variable
        val inputString = bufferedReader.use { it.readText() }
        //Convert the Json File to Gson Object
        val listPointsType = object : TypeToken<MutableList<Point3D>>() {}.type
        return json.fromJson(inputString, listPointsType)
    }
    fun readFromFileToFloats(f: String): MutableList<Float> {
        //Read the file
        val bufferedReader: BufferedReader = File(pathToUnity+f).bufferedReader()
        // Read the text from bufferReader and store in String variable
        val inputString = bufferedReader.use { it.readText() }
        //Convert the Json File to Gson Object
        val listFloatsType = object : TypeToken<MutableList<Float>>() {}.type
        return json.fromJson(inputString, listFloatsType)
    }

}