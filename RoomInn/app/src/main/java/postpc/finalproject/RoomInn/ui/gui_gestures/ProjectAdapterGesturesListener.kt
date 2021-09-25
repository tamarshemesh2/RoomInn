package postpc.finalproject.RoomInn.ui.gui_gestures

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import postpc.finalproject.RoomInn.models.RoomInnApplication
import postpc.finalproject.RoomInn.ui.projectItem.ProjectItem
import postpc.finalproject.RoomInn.ui.projectItem.ProjectItemAdapter
import postpc.finalproject.RoomInn.ui.projectItem.ProjectItemHolder
import kotlin.math.abs

/**
 * Detects left and right swipes across a view.
 */
class ProjectAdapterGesturesListener(
    private val context: Context?,
    private val deleteBtn: ImageButton,
    private val adapter: ProjectItemAdapter,
    private val viewHolder: ProjectItemHolder,
    private val projectItem: ProjectItem
) : View.OnTouchListener {
    private val db = RoomInnApplication.getInstance().getRoomsDB()
    private val gestureDetector: GestureDetector
    fun onSwipeLeft() {
        deleteBtn.animate()
            .translationX(-50f).setDuration(100)
            .alpha(0f).setDuration(100)
        deleteBtn.visibility = View.GONE
        deleteBtn.isClickable = false
    }

    fun onSwipeRight() {
        deleteBtn.animate().translationX(-50f)
        deleteBtn.visibility = View.VISIBLE
        deleteBtn.animate()
            .translationX(0f).setDuration(100)
            .alpha(1f).setDuration(100)
        deleteBtn.isClickable = true
        deleteBtn.setOnClickListener {
            adapter.deleteProject(
                viewHolder.adapterPosition
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            val items = adapter.getItems().filter { it != projectItem.roomName }
            if (viewHolder.projectName.visibility == View.VISIBLE) {
                viewHolder.projectName.visibility = View.GONE
                viewHolder.projectNameEditText.visibility = View.VISIBLE
                viewHolder.projectNameEditText.text = projectItem.roomName
                viewHolder.projectNameEditText.doOnTextChanged { txt, _, _, _ ->
                    //todo: track the typing process, do we need it?
                }
            } else {
                val txt = viewHolder.projectNameEditText.text.toString()
                if (txt in items) {
                    Toast.makeText(context, "the given name is already exists", Toast.LENGTH_SHORT)
                        .show()
                    return true
                }
                db.renameRoom(projectItem.roomName, txt)
//                viewHolder.projectName.text = txt
                viewHolder.projectName.visibility = View.VISIBLE
                viewHolder.projectNameEditText.visibility = View.GONE
            }
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val distanceX: Float = e2.x - e1.x
            val distanceY: Float = e2.y - e1.y
            if (abs(distanceX) > abs(distanceY) &&
                abs(distanceX) > SWIPE_DISTANCE_THRESHOLD &&
                abs(velocityX) > SWIPE_VELOCITY_THRESHOLD
            ) {
                if (distanceX > 0) onSwipeRight() else onSwipeLeft()
                return true
            }
            return false
        }


        private val SWIPE_DISTANCE_THRESHOLD by lazy { 100 }
        private val SWIPE_VELOCITY_THRESHOLD by lazy { 100 }

    }

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }
}