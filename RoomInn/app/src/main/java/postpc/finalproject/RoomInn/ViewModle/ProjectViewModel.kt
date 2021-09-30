package postpc.finalproject.RoomInn.ViewModle

import android.content.Context
import android.os.Build
import android.os.FileObserver
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import me.toptas.fancyshowcase.FancyShowCaseQueue
import postpc.finalproject.RoomInn.furnitureData.Wall
import postpc.finalproject.RoomInn.Room
import postpc.finalproject.RoomInn.furnitureData.Furniture
import postpc.finalproject.RoomInn.furnitureData.Point3D
import postpc.finalproject.RoomInn.models.LoadingStage
import postpc.finalproject.RoomInn.models.RoomInnApplication.Companion.getInstance
import postpc.finalproject.RoomInn.ui.FurnitureOnBoard
import postpc.finalproject.RoomInn.ui.projectItem.ProjectItemAdapter
import kotlin.math.PI
import kotlin.math.asin

class ProjectViewModel : ViewModel() {
    var helpMenuQueue: FancyShowCaseQueue? = null
    var roomEnableFurnitureOnBoard: Boolean = false

    @RequiresApi(Build.VERSION_CODES.N)

    var room: Room = Room()
        set(newRoom: Room) {
            if (field.userId != "user id") {
                getInstance().getRoomsDB().saveRoom(field)
            }
            field = newRoom
//            memoryStack = RoomMemoryStack(newRoom)
        }
    val pointsPathName: String by lazy { "pointData.json" }
    val distancesPathName: String by lazy { "distances.json" }

//    var memoryStack: RoomMemoryStack = RoomMemoryStack(room)
    val redoUndoPresses = MutableLiveData(false)
    var goTo: Int = 0

    var projectName: String = ""
    val layoutMeasures = intArrayOf(0, 0)
    var loadingStage: LoadingStage = LoadingStage.SUCCESS

    var currentPosition: Point3D = Point3D()
    var furniture: Furniture? = null
    var newFurniture: Boolean = true
    var activityContext: Context? = null

    val doorsAndWindows = mutableListOf<FurnitureOnBoard>()


    val adapter: ProjectItemAdapter = ProjectItemAdapter()

    init {
        adapter.setViewModel(this)
        getInstance().getRoomsDB().roomsListenerLambda = {
            adapter.setItems()
            Log.e("door-window", room.windows.toString()) // todo - delete
        }
    }


}