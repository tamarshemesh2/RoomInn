package postpc.finalproject.RoomInn.models

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import postpc.finalproject.RoomInn.Room
import postpc.finalproject.RoomInn.furnitureData.Point3D
import postpc.finalproject.RoomInn.furnitureData.Wall
import java.io.BufferedReader
import java.io.File
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.sqrt


class RoomInnApplication : Application() {
    var pathToUnity: String = ""
    val json = Gson()
    private val pointsPathName: String by lazy { "pointData.json" }
    private val distancesPathName: String by lazy { "distances.json" }


    // provide instance of the class to other classes in the app
    companion object {
        lateinit var sp: SharedPreferences
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
        val inputCleanString = inputString.substring(9, inputString.length - 1)
        Log.e("fileProblem!-inputString", inputCleanString)

        //Convert the Json File to Gson Object
        val listPointsType = object : TypeToken<MutableList<Point3D>>() {}.type
        val done: MutableList<Point3D> = json.fromJson(inputCleanString, listPointsType)
        done.forEach {
            it.multiply(100f).apply {
                y = 0.0
                x *= -1
            }
        }
        return done
    }

    fun createWalls(
        room: Room,
    ): MutableList<Wall> {
        room.Walls.clear()
        val corners = room.Corners

        for (i in 1..room.Corners.size) {
            val last:Point3D
            val first:Point3D
            if (i==room.Corners.size){
                last = Point3D(corners.last())
                first = Point3D(corners.first())
            }else{
                first=Point3D(corners[i])
                last=Point3D(corners[i - 1])
            }

            val wall = Wall()
            wall.position =
                Point3D((Point3D(last).add(first)).multiply(0.5)).apply { y = 0.0 }
            val dist =
                sqrt(((last.x - first.x) * (last.x - first.x)) + ((last.z - first.z) * (last.z - first.z)))
            wall.scale = Point3D(dist / 100f, 10.0, 0.001)
            val sinY =
                (Point3D(last).add(Point3D(first).multiply(-1f)).x / (dist))
            Log.e("sinY", sinY.toString())
            wall.rotation = Point3D(0.0, (asin(abs(sinY)) * (180 / PI)), 0.0)
            if (wall.rotation.y.isNaN()) {
                wall.rotation.y = 90.0
            }
            wall.roomId = room.id
            room.Walls.add(wall)
        }
        return room.Walls
    }

}