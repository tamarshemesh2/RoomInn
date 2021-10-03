package postpc.finalproject.RoomInn.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.RoomCanvas
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.furnitureData.Door
import postpc.finalproject.RoomInn.furnitureData.Point3D
import postpc.finalproject.RoomInn.furnitureData.Window
import postpc.finalproject.RoomInn.models.RoomInnApplication


class FloorPlanPlacingFragment : Fragment() {
    companion object {

        fun newInstance(): FloorPlanPlacingFragment {
            return FloorPlanPlacingFragment()
        }
    }


    private val projectViewModel: ProjectViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ProjectViewModel::class.java)
    }

    //3
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // should be the viewModels room!
        return inflater.inflate(R.layout.fragment_floor_plan_no_doors, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layout = view.findViewById<RelativeLayout>(R.id.floorPlanLayout)
        val roomCanvas = view.findViewById<RoomCanvas>(R.id.room_canvas)
        val addWindowBtn = view.findViewById<ImageButton>(R.id.addWindowButton)
        val addDoorBtn = view.findViewById<ImageButton>(R.id.addDoorButton)
        val titleTxt = view.findViewById<TextView>(R.id.titleTextView)
        val doneFab = view.findViewById<FloatingActionButton>(R.id.done_fab)
        val app by lazy { RoomInnApplication.getInstance() }
        titleTxt.text = projectViewModel.projectName +"\nplace windows and doors"

        val vto = layout.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onGlobalLayout() {
                layout.viewTreeObserver
                    .removeOnGlobalLayoutListener(this)

                roomCanvas.setPath(
                    projectViewModel.room.drawFloorPlan(layout.measuredWidth-10,layout.measuredHeight-10))

                layout.getLocationOnScreen(projectViewModel.layoutMeasures)

                val roomRatio =  projectViewModel.room.getRoomRatio()
                val roomId = projectViewModel.room.id

                for (door in projectViewModel.doorsAndWindows) {
                    val relativeLocation =
                        door.furniture.position.getRelativeLocation(roomRatio, intArrayOf(0,0))
                    FurnitureOnBoard(
                        projectViewModel,
                        requireContext(),
                        door.furniture,
                        layout,
                        relativeLocation.x,
                        relativeLocation.z
                    )
                }

                val roomCenter = Point3D(projectViewModel.room.roomCenterGetter())
                val windLocation = Point3D(roomCenter).apply { this.y -= 50 }
                val roomCenterRelative = roomCenter.getRelativeLocation(roomRatio,  intArrayOf(0,0))

                addDoorBtn.setOnClickListener {
                    projectViewModel.doorsAndWindows += FurnitureOnBoard(
                        projectViewModel,
                        requireContext(),
                        Door(position = roomCenter).apply { this.roomId=roomId },
                        layout,
                        roomCenterRelative.x,
                        roomCenterRelative.z
                    )
                }
                addWindowBtn.setOnClickListener {
                    projectViewModel.doorsAndWindows += FurnitureOnBoard(
                        projectViewModel,
                        requireContext(),
                        Window(position = windLocation).apply { this.roomId=roomId },
                        layout,
                        roomCenterRelative.x,
                        roomCenterRelative.z
                    )
                }
                val roomsDB = RoomInnApplication.getInstance().getRoomsDB()
                doneFab.setOnClickListener {
                    for (item in projectViewModel.doorsAndWindows) {
                        if (item.furniture.type == "Door") {
                            projectViewModel.room.doors.add(item.furniture as Door)
                        } else {
                            projectViewModel.room.windows.add(item.furniture as Window)
                        }
                    }
                    roomsDB.createNewRoom(projectViewModel.room)
                    Navigation.findNavController(view)
                        .navigate(R.id.action_floorPlanPlacingFragment_to_floorPlanFragment)
                }


            }
        })

    }


//        val findViewById = findViewById<View>(R.id.canvas)
//        findViewById.draw(room.drawFloorPlan())

}
//}