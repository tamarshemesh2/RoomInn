package postpc.finalproject.RoomInn.ViewModle

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.postpc.myapplication.furnitureData.Wall
import postpc.finalproject.RoomInn.Room
import postpc.finalproject.RoomInn.furnitureData.Furniture
import postpc.finalproject.RoomInn.furnitureData.Point3D
import postpc.finalproject.RoomInn.models.LoadingStage
import postpc.finalproject.RoomInn.models.RoomInnApplication
import postpc.finalproject.RoomInn.models.RoomInnApplication.Companion.getInstance
import postpc.finalproject.RoomInn.ui.FurnitureOnBoard
import postpc.finalproject.RoomInn.ui.projectItem.ProjectItemAdapter
import kotlin.math.PI
import kotlin.math.asin

class ProjectViewModel: ViewModel() {
    var roomEnableFurnitureOnBoard: Boolean = false
    @RequiresApi(Build.VERSION_CODES.N)

    open var room : Room = Room()
        set(newRoom: Room) {
            if (field.userId != "user id") {
                getInstance().getRoomsDB().saveRoom(field)
            }
            field = newRoom
        }

    lateinit var projectName: String
    val layoutMeasures = intArrayOf(0,0)
    var loadingStage : LoadingStage = LoadingStage.SUCCESS

    var currentPosition: Point3D = Point3D()
    var furniture : Furniture? = null
    var newFurniture: Boolean = true
    val furnitureToErase: MutableLiveData<Boolean> = MutableLiveData(false)

    val doorsAndWindows = mutableListOf<FurnitureOnBoard>()


    val adapter: ProjectItemAdapter = ProjectItemAdapter()

    init {
        adapter.setViewModel(this)
        getInstance().getRoomsDB().roomsListenerLambda = { adapter.setItems() }
    }

    fun createWalls(corners: MutableList<Point3D>, distances: MutableList<Float>): MutableList<Wall> {
        val walls = mutableListOf<Wall>()
        for (i in 1..distances.size){
            var wall = Wall()
            wall.position = Point3D(corners[i-1].add(corners[i]).multiply(0.5f)).apply { this.y=0f }
            wall.scale=Point3D(distances[i-1],270f,25f)
            val sinY =
                Point3D(corners[i - 1]).add(Point3D(corners[i]).multiply(-1f)).x / distances[i - 1]
            wall.rotation = Point3D(0f, asin(sinY) *(180/ PI).toFloat(),0f)
            walls.add(wall)
        }
        return walls
    }
}