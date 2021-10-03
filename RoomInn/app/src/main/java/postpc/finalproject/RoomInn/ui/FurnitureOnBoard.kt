package postpc.finalproject.RoomInn.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.widget.RelativeLayout
import androidx.core.view.GestureDetectorCompat
import postpc.finalproject.RoomInn.FurnitureCanvas
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.furnitureData.Furniture
import postpc.finalproject.RoomInn.models.RoomInnApplication
import postpc.finalproject.RoomInn.models.RoomsDB
import postpc.finalproject.RoomInn.ui.gui_gestures.DragAndScaleListener
import postpc.finalproject.RoomInn.ui.gui_gestures.GeneralGestureListener
import kotlin.math.roundToInt

@SuppressLint("ClickableViewAccessibility")
class FurnitureOnBoard(
    private val projectViewModel: ProjectViewModel,
    private val context: Context,
    var furniture: Furniture,
    private val board: RelativeLayout,
    coorX: Double,//relative location
    coorY: Double,//relative location
    withListeners: Boolean = true
) {
    val margin = 10
    val roomRatio = projectViewModel.room.getRoomRatio()
    private lateinit var imageView: FurnitureCanvas

//
//    fun setFurnitureOnBoard(furniture: Furniture){
//        this.furniture = furniture
//        val relativeLocation = furniture.position.getRelativeLocation(
//            projectViewModel.room.getRoomRatio(),
//            projectViewModel.layoutMeasures
//        )
//        furniturPos(relativeLocation.x,relativeLocation.z)
//    }
//
//    fun furID(): String {
//        return furniture.id
//    }

    /* Use GestureDetectorCompat to detect general gesture take place on the image view. */
    private fun addGeneralGestureListener() {
        // This gesture listener is used with the image view.
        val imageViewGestureListener =
            GeneralGestureListener(context, projectViewModel, furniture, board, imageView)
        val layoutLocation = intArrayOf(0, 0)
        board.getLocationOnScreen(layoutLocation)
        val imageViewDragGestureListener =
            DragAndScaleListener(context, projectViewModel, furniture, board, imageView)
        // Create image view gesture detector.
        val imageViewGestureDetectorCompat =
            GestureDetectorCompat(context, imageViewGestureListener)
        // Set double tap listener.
        imageViewGestureDetectorCompat.setOnDoubleTapListener(imageViewGestureListener)
        // Set a new OnTouchListener to image view.
        imageView.setOnTouchListener { v, motionEvent ->
            projectViewModel.furniture = furniture
            /* When image view ontouch event occurred, call it's gesture detector's onTouchEvent method. */
            if (!imageViewGestureDetectorCompat.onTouchEvent(motionEvent)) {
                imageViewDragGestureListener.onTouch(v, motionEvent)
            }

            // update the furniture in the DB
            if (furniture.type !in listOf("Window","Door")){
            val DB: RoomsDB = RoomInnApplication.getInstance().getRoomsDB()
            furniture = projectViewModel.furniture!!
            DB.furnitureMap[furniture.id] = furniture
            if (furniture.id !in DB.roomToFurnitureMap[projectViewModel.room.id]!!) {
                DB.roomToFurnitureMap[projectViewModel.room.id]!!.add(furniture.id)
            }}
            // Return true to tell android OS this listener has consumed the event, do not need to pass the event to other listeners.
            true
        }
    }

    init {

        createNewImageView()
        board.addView(imageView)

        furniturPos(coorX, coorY)

        if (withListeners) {
            addGeneralGestureListener()
        }
    }

    private fun furniturPos(coorX: Double, coorY: Double) {
        val params = imageView.layoutParams as RelativeLayout.LayoutParams
        if (furniture.type == "Door") {
            params.width = (furniture.scale.x * roomRatio).roundToInt() + margin
            params.height = (furniture.scale.x * roomRatio).roundToInt() + margin
        } else {
            params.width = (furniture.scale.x * roomRatio).roundToInt() + margin
            params.height = (furniture.scale.z * roomRatio).roundToInt() + margin
        }

        params.leftMargin = (coorX).toInt()
        params.topMargin = (coorY).toInt()
        params.rightMargin = 0
        params.bottomMargin = 0

        params.rightMargin = params.leftMargin + 5 * params.width
        params.bottomMargin = params.topMargin + 10 * params.height

        imageView.rotation = furniture.rotation.y.toFloat()
    }

    private fun createNewImageView() {
        imageView = FurnitureCanvas(context)
        imageView.setPaintColor(furniture.color)
        imageView.setPath(furniture.draw(roomRatio, roomRatio))
        imageView.setBackgroundColor(Color.TRANSPARENT)
    }
}
