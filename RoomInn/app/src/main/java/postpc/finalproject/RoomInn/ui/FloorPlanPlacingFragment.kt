package postpc.finalproject.RoomInn.ui

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
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.Room
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layout = view.findViewById<RelativeLayout>(R.id.floorPlanLayout)
        val roomCanvas = view.findViewById<RoomCanvas>(R.id.room_canvas)
        val addWindowBtn = view.findViewById<ImageButton>(R.id.addWindowButton)
        val addDoorBtn = view.findViewById<ImageButton>(R.id.addDoorButton)
        val doneFab = view.findViewById<FloatingActionButton>(R.id.done_fab)
        val app by lazy { RoomInnApplication.getInstance() }

        val vto = layout.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onGlobalLayout() {
                layout.viewTreeObserver
                    .removeOnGlobalLayoutListener(this)

//                reading from a json file- using this tutorial :
//                https://medium.com/nplix/how-to-read-and-write-json-data-in-kotlin-with-gson-c2971fd2d124


                //TODO: find a way to call those lines only after unity is closed

//                val corners = app.readFromFileToPoints(pointsPathName)
//                val distances = app.readFromFileToFloats(distancesPathName)
//                val walls = projectViewModel.createWalls(corners, distances)
//                projectViewModel.room = Room(corners,walls)

                roomCanvas.setPath(
                    projectViewModel.room.drawFloorPlan(layout.measuredWidth,layout.measuredHeight))

                layout.getLocationOnScreen(projectViewModel.layoutMeasures)

                val roomRatio =  projectViewModel.room.getRoomRatio()

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

                val roomCenter = Point3D(projectViewModel.room.getRoomCenter())
                val windLocation = Point3D(roomCenter).apply { this.y = 120f }
                val roomCenterRelative = roomCenter.getRelativeLocation(roomRatio,  intArrayOf(0,0))

                addDoorBtn.setOnClickListener {
                    projectViewModel.doorsAndWindows += FurnitureOnBoard(
                        projectViewModel,
                        requireContext(),
                        Door(position = roomCenter),
                        layout,
                        roomCenterRelative.x,
                        roomCenterRelative.z
                    )
                }
                addWindowBtn.setOnClickListener {
                    projectViewModel.doorsAndWindows += FurnitureOnBoard(
                        projectViewModel,
                        requireContext(),
                        Window(position = windLocation),
                        layout,
                        roomCenterRelative.x,
                        roomCenterRelative.z
                    )
                }
                doneFab.setOnClickListener {
                    for (item in projectViewModel.doorsAndWindows) {
                        if (item.furniture.type == "Door") {
                            projectViewModel.room.doors += item.furniture as Door
                        } else {
                            projectViewModel.room.windows += item.furniture as Window
                        }
                    }
                    RoomInnApplication.getInstance().getRoomsDB().updateRoom(projectViewModel.room)
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