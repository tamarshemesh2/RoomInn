package postpc.finalproject.RoomInn.models

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import postpc.finalproject.RoomInn.Room
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.furnitureData.Point3D
import postpc.finalproject.RoomInn.furnitureData.Wall
import java.io.BufferedReader
import java.io.File
import kotlin.math.PI
import kotlin.math.asin


class RoomInnApplication: Application() {
    var pathToUnity: String = ""
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
        val file = File("$pathToUnity/$f")
        Log.e("fileProblem!", file.exists().toString())
        if (!file.exists()) {
            Log.e("fileProblem!", file.absolutePath)
            return mutableListOf()
        }
        //Read the file
        Log.e("fileProblem!", file.absolutePath)
        val bufferedReader: BufferedReader = file.bufferedReader()
        // Read the text from bufferReader and store in String variable
        val inputString = bufferedReader.use { it.readText() }
        val inputCleanString = inputString.substring(9,inputString.length-1)
        Log.e("fileProblem!-inputString", inputCleanString)

        //Convert the Json File to Gson Object
        val listPointsType = object : TypeToken<MutableList<Point3D>>() {}.type
        val done : MutableList<Point3D> = json.fromJson(inputCleanString, listPointsType)
        done.forEach { it.multiply(100f).apply { y=0f } }
        return done
    }
    fun readFromFileToFloats(f: String): MutableList<Float> {
        val file = File("$pathToUnity/$f")
        Log.e("fileProblem!", file.exists().toString())
        if (!file.exists()){
            Log.e("fileProblem!", file.absolutePath)
            return mutableListOf()
        }
        //Read the file
        Log.e("fileProblem!", file.absolutePath)
        //Read the file
        val bufferedReader: BufferedReader = file.bufferedReader()
        // Read the text from bufferReader and store in String variable
        val inputString = bufferedReader.use { it.readText() }
        val inputCleanString = inputString.substring(9,inputString.length-1)
        Log.e("fileProblem!-inputString", inputString)
        Log.e("fileProblem!-inputString", inputCleanString)

        //Convert the Json File to Gson Object
        val listFloatsType = object : TypeToken<MutableList<String>>() {}.type
        val fromJson: MutableList<String> = json.fromJson(inputCleanString, listFloatsType)
        val result = mutableListOf<Float>()
        val deli = " <size=2>"
        fromJson.forEach {
            val split = it.split(deli)
            if (split[1]=="cm"){
                result.add(split[0].toFloat() / 100f)
            }else{
                result.add(split[0].toFloat() )
            }}
        Log.e("fileProblem!-inputString", result.toString())

        return result
    }
    fun createWalls(
        corners: MutableList<Point3D>,
        distances: MutableList<Float>,
         projectViewModel : ProjectViewModel
    ): MutableList<Wall> {
        val walls = mutableListOf<Wall>()
        for (i in 1..distances.size) {
            var wall = Wall()
            wall.position =
                Point3D(corners[i - 1].add(corners[i]).multiply(0.5f)).apply { this.y = 0f }
            wall.scale = Point3D(distances[i - 1] , 10f, 0.001f)
            val sinY =
                Point3D(corners[i - 1]).add(Point3D(corners[i]).multiply(-1f)).x / (distances[i - 1] * 100)
            Log.e("sinY", sinY.toString())
            wall.rotation = Point3D(0f, asin(sinY) * (180 / PI).toFloat(), 0f)
            wall.roomCenter = Point3D(projectViewModel.room.getRoomCenter())
            walls.add(wall)
        }
        return walls
    }

}