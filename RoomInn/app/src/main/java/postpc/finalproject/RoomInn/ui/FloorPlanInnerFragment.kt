package postpc.finalproject.RoomInn.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ProgressBar
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.RoomCanvas
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.models.RoomInnApplication
import kotlin.math.roundToInt


class FloorPlanInnerFragment : Fragment() {
    //2
    companion object {

        fun newInstance(): FloorPlanInnerFragment {
            return FloorPlanInnerFragment()
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
//        room.drawFloorPlan()
        // should be the viewModels room!
        return inflater.inflate(R.layout.fragment_inner_floor_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layout = view.findViewById<RelativeLayout>(R.id.room_layout)
        val roomCanvas = view.findViewById<RoomCanvas>(R.id.room_canvas)
        val loadingBar = view.findViewById<ProgressBar>(R.id.progress_bar)
        loadingBar.visibility = View.VISIBLE

        val vto = layout.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                layout.viewTreeObserver
                    .removeOnGlobalLayoutListener(this)
                roomCanvas.setPath(
                    projectViewModel.room.drawFloorPlan(
                        layout.measuredWidth,
                        layout.measuredHeight
                    )
                )

                loadingBar.visibility = View.GONE
                val roomRatio = projectViewModel.room.getRoomRatio()
                layout.getLocationOnScreen(projectViewModel.layoutMeasures)

                for (door in projectViewModel.room.doors) {
                    val relativeLocation =
                        door.position.getRelativeLocation(roomRatio, intArrayOf(0,0))
                    FurnitureOnBoard(
                        projectViewModel,
                        requireContext(),
                        door,
                        layout,
                        relativeLocation.x,
                        relativeLocation.z, false
                    )
                }
                for (window in projectViewModel.room.windows) {
                    val relativeLocation =
                        window.position.getRelativeLocation(roomRatio, intArrayOf(0,0))
                    FurnitureOnBoard(
                        projectViewModel,
                        requireContext(),
                        window,
                        layout,
                        relativeLocation.x,
                        relativeLocation.z, false
                    )
                }
                for (furId in RoomInnApplication.getInstance()
                    .getRoomsDB().roomToFurnitureMap[projectViewModel.room.id]!!) {
                    val fur = RoomInnApplication.getInstance().getRoomsDB().furnitureMap[furId]
                    if (fur != null) {
                        if (fur.type !in listOf("Window", "Door")) {
                            val relativeLocation =
                                fur.position.getRelativeLocation(roomRatio, intArrayOf(0,0))
                            FurnitureOnBoard(
                                projectViewModel,
                                requireContext(),
                                fur,
                                layout,
                                relativeLocation.x,
                                relativeLocation.z
                            )
                        }
                    }
                }
            }
        })

    }


//        val findViewById = findViewById<View>(R.id.canvas)
//        findViewById.draw(room.drawFloorPlan())

}
//}