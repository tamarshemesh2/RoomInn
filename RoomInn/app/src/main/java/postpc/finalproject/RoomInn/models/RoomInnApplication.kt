package postpc.finalproject.RoomInn.models

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import postpc.finalproject.RoomInn.Room
import postpc.finalproject.RoomInn.furnitureData.Point3D
import postpc.finalproject.RoomInn.furnitureData.Wall
import java.io.BufferedReader
import java.io.File
import kotlin.math.PI
import kotlin.math.asin


class RoomInnApplication: Application() {
    val pathToUnity: MutableLiveData<String> = MutableLiveData("")
    val json = Gson()


    // provide instance of the class to other classes in the app
    companion object {
        lateinit var sp:SharedPreferences
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
        sp = this.getSharedPreferences("local_DB", Context.MODE_PRIVATE)
        roomsDB = RoomsDB(this)

    }

    fun saveToSP(){
        val editor = sp.edit()
        editor.clear()
        val serializedState: Map<String,String> = roomsDB.serializeState()
        for ((key,value) in serializedState){
            Log.d("furnitureSP", value)

            editor.putString(key, value)
        }
        editor.apply()
    }


    fun readFromFileToPoints(f: String): MutableList<Point3D> {
        //Read the file
        val bufferedReader: BufferedReader = File(pathToUnity.value+f).bufferedReader()
        // Read the text from bufferReader and store in String variable
        val inputString = bufferedReader.use { it.readText() }
        //Convert the Json File to Gson Object
        val listPointsType = object : TypeToken<MutableList<Point3D>>() {}.type
        return json.fromJson(inputString, listPointsType)
    }
    fun readFromFileToFloats(f: String): MutableList<Float> {
        //Read the file
        val bufferedReader: BufferedReader = File(pathToUnity.value+f).bufferedReader()
        // Read the text from bufferReader and store in String variable
        val inputString = bufferedReader.use { it.readText() }
        //Convert the Json File to Gson Object
        val listFloatsType = object : TypeToken<MutableList<Float>>() {}.type
        return json.fromJson(inputString, listFloatsType)
    }
    fun createWalls(
        corners: MutableList<Point3D>,
        distances: MutableList<Float>
    ): MutableList<Wall> {
        val walls = mutableListOf<Wall>()
        for (i in 1..distances.size) {
            var wall = Wall()
            wall.position =
                Point3D(corners[i - 1].add(corners[i]).multiply(0.5f)).apply { this.y = 0f }
            wall.scale = Point3D(distances[i - 1], 270f, 25f)
            val sinY =
                Point3D(corners[i - 1]).add(Point3D(corners[i]).multiply(-1f)).x / distances[i - 1]
            wall.rotation = Point3D(0f, asin(sinY) * (180 / PI).toFloat(), 0f)
            walls.add(wall)
        }
        return walls
    }

}