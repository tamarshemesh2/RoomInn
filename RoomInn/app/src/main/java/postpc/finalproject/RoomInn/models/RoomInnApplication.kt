package postpc.finalproject.RoomInn.models

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
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
import kotlin.math.sqrt


class RoomInnApplication: Application() {
    var pathToUnity: String = ""
    val json = Gson()
    private val pointsPathName: String by lazy { "pointData.json" }
    private val distancesPathName: String by lazy { "distances.json" }



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


    fun readFromFileToPoints(): MutableList<Point3D> {
        val file = File("$pathToUnity/$pointsPathName")
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
        done.forEach { it.multiply(100f).apply { y=0f
        x*=-1} }
        return done
    }
    fun readFromFileToFloats(): MutableList<Float> {
        val file = File("$pathToUnity/$distancesPathName")
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
        room: Room,
        distances: MutableList<Float>,
    ): MutableList<Wall> {
        val corners = room.Corners
        val walls = mutableListOf<Wall>()
        for (i in 1..distances.size) {
            val wall = Wall()
            wall.position =
                Point3D(Point3D(corners[i - 1]).add(Point3D(corners[i])).multiply(0.5f)).apply { y = 0f }
            wall.scale = Point3D(distances[i - 1] , 10f, 0.001f)
            val sinY =
                Point3D(corners[i - 1]).add(Point3D(corners[i]).multiply(-1f)).x / (distances[i - 1] * 100)
            Log.e("sinY", sinY.toString())
            wall.rotation = Point3D(0f, asin(sinY) * (180 / PI).toFloat(), 0f)
            if (wall.rotation.y.isNaN()) {
                wall.rotation.y = 90f
            }
            wall.roomId = room.id
            walls.add(wall)
            Log.d("create new wall", wall.toString())
        }
        val wall = Wall()
        val last = Point3D(corners.last())
        val first = Point3D(corners.first())
        wall.position =
                Point3D((Point3D(last).add(first)).multiply(0.5f)).apply { y = 0f }
        val dist = sqrt(((last.x - first.x)*(last.x - first.x)) + ((last.z - first.z)*(last.z - first.z)))/100f
        wall.scale = Point3D(dist, 10f, 0.001f)
        val sinY =
                Point3D(last).add(Point3D(first).multiply(-1f)).x / (dist * 100)
        Log.e("sinY", sinY.toString())
        wall.rotation = Point3D(0f, asin(sinY) * (180 / PI).toFloat(), 0f)
        if (wall.rotation.y.isNaN()) {
            wall.rotation.y = 90f
        }
        wall.roomId = room.id
        walls.add(wall)
        Log.d("create new wall", wall.toString())

        room.Walls = walls
        return walls
    }

}