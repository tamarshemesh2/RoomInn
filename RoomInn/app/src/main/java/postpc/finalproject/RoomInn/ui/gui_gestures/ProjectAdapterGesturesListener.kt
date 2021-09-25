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
import kotlin.math.sqrt

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
            .translationX(-50f).setDuration(600).withEndAction {
                deleteBtn.visibility = View.GONE
                deleteBtn.isClickable = false
            }.start()

    }

    fun onSwipeRight() {
        deleteBtn.animate().translationX(-50f).start()
        deleteBtn.visibility = View.VISIBLE
        deleteBtn.animate()
            .translationX(10f).setDuration(600)
            .withEndAction {
                deleteBtn.animate()
                    .translationX(0f).setDuration(100).start()
                deleteBtn.isClickable = true
                deleteBtn.setOnClickListener {
                    adapter.deleteProject(
                        viewHolder.adapterPosition)}
            }.start()
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

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (e1 != null && e2 != null) {
                val dx = e2.rawX - e1.rawX
                val dy = e2.rawY - e1.rawY
                if (sqrt((dx * dx) + (dy * dy)) > 10) {
                    if (dx > 0) onSwipeRight() else onSwipeLeft()
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }


        private val SWIPE_DISTANCE_THRESHOLD by lazy { 100 }
        private val SWIPE_VELOCITY_THRESHOLD by lazy { 100 }

    }

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }
}