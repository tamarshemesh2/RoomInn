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
                    //TODO: comment the next lines
                    corners = mutableListOf(
                        Point3D(
                            -0.015324175357818604f,
                            -0.8654714822769165f,
                            1.9097247123718262f
                        ).multiply(100f),
                        Point3D(
                            0.8313037157058716f,
                            -1.3702747821807862f,
                            -0.5127741098403931f
                        ).multiply(100f),
                        Point3D(
                            -2.139707326889038f,
                            -0.8447096347808838f,
                            -1.2366341352462769f
                        ).multiply(100f),
                        Point3D(
                            -2.084784984588623f,
                            -0.5958563089370728f,
                            1.5056521892547608f
                        ).multiply(100f)

                    )
//                    val listPointsType = object : TypeToken<MutableMap<String, MutableList<Point3D>>>() {}.type
                    val done  = app.json.toJson(corners)
                    Log.e("fileProblem!-needed Input", done)
                    //todo -- stop comment here and uncomment the next

//                    Navigation.findNavController(view).navigate(R.id.action_floorPlanRotateFragment_to_profileFragment2)
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
                projectNameEditText.doOnTextChanged { text, start, before, count ->
                    val projectName = text.toString()
                    if (projectName in roomsDB.rooms.value!!
                    ) {
                        projectNameEditText.error = "already in use for another project"
                    }
                }

                roomCanvas.getLocationOnScreen(projectViewModel.layoutMeasures)

                val roomCenterPoint = Point3D(
                    projectViewModel.layoutMeasures[0] + 0.5f * (roomCanvas.measuredWidth),
                    0f,
                    projectViewModel.layoutMeasures[1] + 0.5f * (roomCanvas.measuredHeight)
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
                    rotateEditText.setText(roomCanvas.rotation.toString())
                }
                rotateLeftBtn.setOnClickListener {
                    roomCanvas.rotation -= 10
                    rotateEditText.setText(roomCanvas.rotation.toString())

                }



                doneFab.setOnClickListener {
                    if (projectNameEditText.text.toString()==""){
                        projectNameEditText.error= "No value entered"
                    }
                    if (projectNameEditText.error == null) {
                        projectViewModel.room.rotateRoomCornersByAngle(
                            roomCenterPoint,
                            ((roomCanvas.rotation * PI) / 180).toFloat()
                        )
                        projectViewModel.room.name = projectNameEditText.text.toString()
                        val distancesFromFile =
                            app.readFromFileToFloats(projectViewModel.distancesPathName)
                        Log.d("fileProblem!", "done ")
                        projectViewModel.room.Walls = app.createWalls(corners,distancesFromFile)
                        roomsDB.createNewRoom(projectViewModel.room)

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