package postpc.finalproject.RoomInn.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.furnitureData.Point3D
import postpc.finalproject.RoomInn.models.RoomInnApplication
import java.util.*


class FloorPlanFragment : Fragment() {
    var furnitureList: List<FurnitureOnBoard> = listOf()
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var hamburger: ImageView;

    companion object {
        fun newInstance() = FloorPlanFragment()
    }

    private val projectViewModel: ProjectViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ProjectViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_floor_plan, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // set the view direction -> LTR
        this.activity?.window?.decorView?.layoutDirection = View.LAYOUT_DIRECTION_LTR;

        // TODO: finish finding all the views
        // find all views
        val roomCanvas: FragmentContainerView = view.findViewById(R.id.floorPlan)
        val roomLayout: RelativeLayout = view.findViewById(R.id.floorPlanLayout)
        val roomTitle: TextView = view.findViewById(R.id.titleTextView)
        val addFab: ImageButton = view.findViewById(R.id.addButton)
        val playButton: FloatingActionButton = view.findViewById(R.id.playButton)
        val redoButton: FloatingActionButton = view.findViewById(R.id.redoButton)
        val undoButton: FloatingActionButton = view.findViewById(R.id.undoButton)
        var toAddFurniture = false
        //add all furniture to board

        // find hamburger views:
        drawerLayout = view.findViewById(R.id.draw_layout)
        navigationView = view.findViewById(R.id.hamburger_settings_navigation_layout)
        hamburger = view.findViewById(R.id.hamburgerMenuButton)

        roomTitle.text = "${projectViewModel.projectName} \n- Floor Plan"

        playButton.setOnClickListener {
            val intent = Intent(context, UnityHandler::class.java)
            intent.putExtra("Scene Index", MainUnityPlayerActivity.sceneIndex)
            intent.putExtra("Room Name", projectViewModel!!.room.name)
            startActivity(intent)
        }

        val vto = roomLayout.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                roomLayout.viewTreeObserver
                    .removeOnGlobalLayoutListener(this)

                roomCanvas.setOnTouchListener { v, event ->
                    if (toAddFurniture) {
                        projectViewModel.currentPosition =
                            Point3D(event.rawX, 0f, event.rawY).toAbsolutLocation(
                                projectViewModel.room.getRoomRatio(),
                                projectViewModel.layoutMeasures
                            )
                        projectViewModel.newFurniture = true
                        Navigation.findNavController(v)
                            .navigate(R.id.action_floorPlanFragment_to_addFurnitureFragment2)
                    }
                    toAddFurniture = false
                    return@setOnTouchListener true
                }
                addFab.setOnClickListener {
                    Toast.makeText(
                        requireContext(),
                        "Tap where you wish to place a new furniture",
                        Toast.LENGTH_SHORT
                    ).show()
                    toAddFurniture = true
                }
                undoButton.setOnClickListener {
                    if (!projectViewModel.memoryStack.undoStep()){
                    Toast.makeText(requireContext(),"there is no step to undo",Toast.LENGTH_SHORT).show()}
                    projectViewModel.redoUndoPresses.value=true
                }
                redoButton.setOnClickListener {
                    if(!projectViewModel.memoryStack.redoStep()){
                        Toast.makeText(requireContext(),"there is no step to redo",Toast.LENGTH_SHORT).show()}
                    projectViewModel.redoUndoPresses.value=true


                }
            }
        })
        hamburger.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}



