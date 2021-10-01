package postpc.finalproject.RoomInn.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Chronometer
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.reflect.TypeToken
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.Room
import postpc.finalproject.RoomInn.RoomCanvas
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.furnitureData.Door
import postpc.finalproject.RoomInn.furnitureData.Point3D
import postpc.finalproject.RoomInn.furnitureData.Window
import postpc.finalproject.RoomInn.models.RoomInnApplication
import postpc.finalproject.RoomInn.ui.gui_gestures.DragAndScaleListener
import kotlin.math.PI
import kotlin.math.min
import kotlin.math.roundToInt


class FloorPlanRotateFragment : Fragment() {
    companion object {

        fun newInstance(): FloorPlanRotateFragment {
            return FloorPlanRotateFragment()
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
        this.activity?.window?.decorView?.layoutDirection = View.LAYOUT_DIRECTION_LTR;

        // should be the viewModels room!
        return inflater.inflate(R.layout.fragment_floor_plan_rotate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val app = RoomInnApplication.getInstance()

        var corners =app.readFromFileToPoints(projectViewModel.pointsPathName)

        super.onViewCreated(view, savedInstanceState)
        this.activity?.window?.decorView?.layoutDirection = View.LAYOUT_DIRECTION_LTR;


        val roomCanvas = view.findViewById<RoomCanvas>(R.id.room_canvas)
        val rotateRightBtn = view.findViewById<ImageButton>(R.id.rotateRight)
        val rotateLeftBtn = view.findViewById<ImageButton>(R.id.rotateLeft)
        val rotateEditText = view.findViewById<EditText>(R.id.rotateEditText)
        val projectNameEditText = view.findViewById<EditText>(R.id.giveProjectNameEditText)
        val doneFab = view.findViewById<FloatingActionButton>(R.id.done_fab)
        val roomsDB = app.getRoomsDB()

        val vto = roomCanvas.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {

            @SuppressLint("ClickableViewAccessibility")
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onGlobalLayout() {
                roomCanvas.viewTreeObserver
                    .removeOnGlobalLayoutListener(this)
                if (corners.size <2){
                    Navigation.findNavController(view).navigate(R.id.action_floorPlanRotateFragment_to_profileFragment2)
                }
                val room = Room(Corners = corners, userId = roomsDB.user.id)

                projectViewModel.room = room
                val minSize = min(roomCanvas.measuredWidth, roomCanvas.measuredHeight)

                roomCanvas.setPath(
                    projectViewModel.room.drawFloorPlan(
                        minSize,
                        minSize
                    )
                )
                projectNameEditText.setOnFocusChangeListener { v, hasFocus ->
                    v.dispatchDisplayHint(View.GONE)
                }
                projectNameEditText.doOnTextChanged { text, start, before, count ->
                    val projectName = text.toString()
                    if (projectName in roomsDB.rooms.value!!
                    ) {
                        projectNameEditText.error = "already in use for another project"
                    }
                }

                roomCanvas.getLocationOnScreen(projectViewModel.layoutMeasures)

                val roomCenterPoint = Point3D(
                    projectViewModel.layoutMeasures[0] + (0.5f * (roomCanvas.measuredWidth)),
                    0f,
                    projectViewModel.layoutMeasures[1] + (0.5f * (roomCanvas.measuredHeight))
                ).toAbsolutLocation(
                    projectViewModel.room.getRoomRatio(),
                    projectViewModel.layoutMeasures
                )

                rotateEditText.setText("0")

                rotateEditText.doOnTextChanged { text, start, before, count ->
                    var toString = text.toString()
                    if (toString == "") {
                        toString = "0"
                    }
                    roomCanvas.rotation = toString.toFloat() % 360
                }

                rotateEditText.setOnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        rotateEditText.setText(roomCanvas.rotation.toString())
                    }
                }


                rotateRightBtn.setOnClickListener {
                    roomCanvas.rotation += 10
                    roomCanvas.rotation%=360

                    rotateEditText.setText(roomCanvas.rotation.toString())
                }
                rotateLeftBtn.setOnClickListener {
                    roomCanvas.rotation -= 10
                    roomCanvas.rotation%=360
                    rotateEditText.setText(roomCanvas.rotation.toString())

                }



                doneFab.setOnClickListener {
                    if (projectNameEditText.text.toString()==""){
                        projectNameEditText.error= "No value entered"
                    }
                    if (projectNameEditText.error == null) {
                        projectViewModel.room.rotateRoomCornersByAngle(
                            ((roomCanvas.rotation * PI.toFloat()) / 180f),roomCenterPoint
                        )
                        projectViewModel.room.name = projectNameEditText.text.toString()
                        val distancesFromFile =
                            app.readFromFileToFloats(projectViewModel.distancesPathName)
                        projectViewModel.room.Walls = app.createWalls(corners,distancesFromFile, projectViewModel)
                        Navigation.findNavController(view)
                            .navigate(R.id.action_floorPlanRotateFragment_to_floorPlanPlacingFragment)
                    }
                }
            }
        })

    }


//        val findViewById = findViewById<View>(R.id.canvas)
//        findViewById.draw(room.drawFloorPlan())

}
//}