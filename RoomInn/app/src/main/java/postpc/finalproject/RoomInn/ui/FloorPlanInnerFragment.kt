package postpc.finalproject.RoomInn.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.RoomCanvas
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.models.RoomInnApplication


class FloorPlanInnerFragment : Fragment() {
    companion object {
        fun newInstance(): FloorPlanInnerFragment {
            return FloorPlanInnerFragment()
        }
    }

    private val projectViewModel: ProjectViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ProjectViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // should be the viewModels room!
        return inflater.inflate(R.layout.fragment_inner_floor_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layout = view.findViewById<RelativeLayout>(R.id.room_layout)
        val roomCanvas = view.findViewById<RoomCanvas>(R.id.room_canvas)
        val furnitureOnBoardList = mutableListOf<FurnitureOnBoard>()
        val room = projectViewModel.room


        val vto = layout.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                layout.viewTreeObserver
                    .removeOnGlobalLayoutListener(this)
                roomCanvas.setPath(
                    room.drawFloorPlan(
                        layout.measuredWidth,
                        layout.measuredHeight
                    )
                )
                val roomRatio = room.getRoomRatio()
                layout.getLocationOnScreen(projectViewModel.layoutMeasures)
                for (door in room.doors) {
                    val relativeLocation =
                        door.position.getRelativeLocation(roomRatio, intArrayOf(0, 0))
                    FurnitureOnBoard(
                        projectViewModel,
                        requireContext(),
                        door,
                        layout,
                        relativeLocation.x,
                        relativeLocation.z, false
                    )
                }
                for (window in room.windows) {
                    val relativeLocation =
                        window.position.getRelativeLocation(roomRatio, intArrayOf(0, 0))
                    FurnitureOnBoard(
                        projectViewModel,
                        requireContext(),
                        window,
                        layout,
                        relativeLocation.x,
                        relativeLocation.z, false
                    )
                }
                loadAllFurnitureToBoard(roomRatio, furnitureOnBoardList, layout)
                projectViewModel.memoryStack.saveRoomChange()
                projectViewModel.redoUndoPresses.observeForever {
                    if (it) {
                        layout.removeViews(
                            1 + room.windows.size + room.doors.size,
                            furnitureOnBoardList.size
                        )
                        furnitureOnBoardList.clear()
                        loadAllFurnitureToBoard(room.getRoomRatio(), furnitureOnBoardList, layout)
                        projectViewModel.memoryStack.saveRoomChange()
                        projectViewModel.redoUndoPresses.value = false
                    }
                }
            }
        })
    }

    private fun loadAllFurnitureToBoard(
        roomRatio: Float,
        furnitureOnBoardList: MutableList<FurnitureOnBoard>,
        layout: RelativeLayout
    ) {
        val roomsDB = RoomInnApplication.getInstance()
            .getRoomsDB()
        val mutableList = roomsDB.roomToFurnitureMap[projectViewModel.room.id]!!
        for (furId in mutableList) {
            val fur = roomsDB.furnitureMap[furId]
            if (fur != null) {
                if (fur.type !in listOf("Window", "Door")) {
                    val relativeLocation =
                        fur.position.getRelativeLocation(roomRatio, intArrayOf(0, 0))
                    furnitureOnBoardList += FurnitureOnBoard(
                        projectViewModel,
                        requireParentFragment().requireContext(),
                        fur,
                        layout,
                        relativeLocation.x,
                        relativeLocation.z
                    )
                }
            }
        }
    }
}
