package postpc.finalproject.RoomInn.ui.gui_gestures

import android.content.Context
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.widget.RelativeLayout
import androidx.navigation.Navigation
import postpc.finalproject.RoomInn.FurnitureCanvas
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.furnitureData.Furniture

class GeneralGestureListener(
    private var context: Context?,
    private var projectViewModel: ProjectViewModel,
    private var furniture: Furniture,
    private var board: RelativeLayout,
    private val imageView: FurnitureCanvas
) :
    SimpleOnGestureListener() {


    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        furniture.rotation.y += 45
        furniture.rotation.y %= 360
        imageView.rotation = furniture.rotation.y.toFloat()
        return super.onSingleTapConfirmed(e)
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        if (furniture.type in listOf("Door", "Window") && projectViewModel.newFurniture) {
            Navigation.findNavController(board)
                .navigate(R.id.action_floorPlanPlacingFragment_to_editFurnitureFragment)
        } else {
            Navigation.findNavController(board)
                .navigate(R.id.action_floorPlanFragment_to_editFurnitureFragment)
        }
        projectViewModel.newFurniture = false

        return true
    }


}
