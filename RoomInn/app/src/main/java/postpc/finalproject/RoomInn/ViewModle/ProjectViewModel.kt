package postpc.finalproject.RoomInn.ViewModle

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import postpc.finalproject.RoomInn.Room
import postpc.finalproject.RoomInn.furnitureData.Furniture
import postpc.finalproject.RoomInn.furnitureData.Point3D
import postpc.finalproject.RoomInn.models.LoadingStage
import postpc.finalproject.RoomInn.models.RoomInnApplication
import postpc.finalproject.RoomInn.models.RoomInnApplication.Companion.getInstance
import postpc.finalproject.RoomInn.ui.FurnitureOnBoard
import postpc.finalproject.RoomInn.ui.projectItem.ProjectItemAdapter

class ProjectViewModel: ViewModel() {
    var roomEnableFurnitureOnBoard: Boolean = false
    @RequiresApi(Build.VERSION_CODES.N)
    var room : Room = Room()  // TODO: change ro live data, so that each time it changes we save to the DB the room and the furniture of the room
    lateinit var projectName: String
    val layoutMeasures = intArrayOf(0,0)
    var loadingStage : LoadingStage = LoadingStage.SUCCESS

    var currentPosition: Point3D = Point3D()
    var currentX :Float = 0f
    var currentY : Float = 0f
    var furniture : Furniture? = null
    var newFurniture: Boolean = true
    val furnitureToErase: MutableLiveData<Boolean> = MutableLiveData(false)

    val doorsAndWindows = mutableListOf<FurnitureOnBoard>()


    val adapter: ProjectItemAdapter = ProjectItemAdapter()

    init {
        adapter.setViewModel(this)
        getInstance().getRoomsDB().roomsListenerLambda = { adapter.setItems() }
    }
}