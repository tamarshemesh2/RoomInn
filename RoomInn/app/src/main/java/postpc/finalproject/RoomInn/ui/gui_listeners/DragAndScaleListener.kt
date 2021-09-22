package postpc.finalproject.RoomInn.ui.gui_listeners

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import postpc.finalproject.RoomInn.FurnitureCanvas
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.furnitureData.Furniture
import postpc.finalproject.RoomInn.furnitureData.Point3D
import kotlin.math.sqrt

/**
 * The scale listener, used for handling multi-finger scale gestures.
 */
class DragAndScaleListener(
    private var context: Context?,
    private var projectViewModel: ProjectViewModel,
    private var furniture: Furniture,
    private var board: RelativeLayout,
    private val imageView: FurnitureCanvas,

    ) : OnTouchListener {
    private val NONE = 0
    private val DRAG = 1
    private val ZOOM = 2

    private var mode = NONE
    private var oldDist = 1f
    private var scaleDiff = 0f

    var params = imageView.layoutParams as RelativeLayout.LayoutParams
    var startWidth = 0
    var startHeight = 0
    var dx = 0f
    var dy = 0f
    var x = 0f
    var y = 0f

    @RequiresApi(Build.VERSION_CODES.N)
    private val roomRatio = projectViewModel.room.getRoomRatio()


    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt((x * x + y * y).toDouble()).toFloat()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val view = v as ImageView
        projectViewModel.furniture = furniture
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                params = view.layoutParams as RelativeLayout.LayoutParams
                startWidth = params.width
                startHeight = params.height
                dx = event.rawX - params.leftMargin
                dy = event.rawY - params.topMargin
                mode = DRAG
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(event)
                if (oldDist > 10f) {
                    mode = ZOOM
                } else {
                    Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show()
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> mode = NONE

            MotionEvent.ACTION_MOVE -> if (mode == DRAG) {
                x = event.rawX
                y = event.rawY
                params.leftMargin = ((x - dx).toInt())
                params.topMargin = ((y - dy).toInt())
                furniture.position.x = (x - dx)
                furniture.position.z = (y - dy)
                params.rightMargin = 0
                params.bottomMargin = 0
                params.rightMargin = params.leftMargin + 5 * params.width
                params.bottomMargin = params.topMargin + 10 * params.height
                view.layoutParams = params
            } else if (mode == ZOOM) {

                if (event.pointerCount == 2) {
                    x = event.rawX
                    y = event.rawY
                    val newDist = spacing(event)
                    if (newDist > 10f) {
                        val scale = newDist / oldDist * view.scaleX
                        if (scale > 0.6) {
                            // updates the furniture data according to the scale
                            furniture.scale(newDist / oldDist)
                            scaleDiff = scale
                            view.scaleX = scale
                            view.scaleY = scale

                        }
                    }
                    x = event.rawX
                    y = event.rawY
                    params.leftMargin = (x - dx - (scaleDiff)).toInt()
                    params.topMargin = (y - dy - (scaleDiff)).toInt()
                    params.rightMargin = 0
                    params.bottomMargin = 0
                    params.rightMargin = params.leftMargin + 5 * params.width
                    params.bottomMargin = params.topMargin + 10 * params.height
                    view.layoutParams = params

                }
            }
        }
        furniture.position = Point3D(
            params.leftMargin.toFloat(),
            furniture.position.y,
            params.topMargin.toFloat()
        ).toAbsolutLocation(roomRatio, intArrayOf(0,0))
        projectViewModel.furniture = furniture
        return true
    }

}


