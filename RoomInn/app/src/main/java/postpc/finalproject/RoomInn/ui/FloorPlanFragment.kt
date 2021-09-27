package postpc.finalproject.RoomInn.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.furnitureData.Point3D
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
        val roomCanvas: View? = view.findViewById(R.id.floorPlan)
        val roomLayout: RelativeLayout = view.findViewById(R.id.floorPlanLayout)
        val roomTitle: TextView = view.findViewById(R.id.titleTextView)
        val eraseImg: ImageView = view.findViewById(R.id.deleteImageView)
        val addFab: ImageButton = view.findViewById(R.id.addButton)
        val playButton: FloatingActionButton = view.findViewById(R.id.playButton)
        var toAddFurniture = false
        //add all furniture to board

        // find hamburger views:
        drawerLayout = view.findViewById(R.id.draw_layout)
        navigationView = view.findViewById(R.id.hamburger_settings_navigation_layout)
        hamburger = view.findViewById(R.id.hamburgerMenuButton)

        roomTitle.text = "${projectViewModel.projectName} \n- Floor Plan"

        playButton.setOnClickListener {
            val intent = Intent(context, UnityHandler::class.java)
            intent.putExtra("Scene Index", RoomUnityPlayerActivity.sceneIndex)
            intent.putExtra("Room Name", projectViewModel!!.room.name)
            startActivity(intent)
        }

        val vto = roomLayout.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                roomLayout.viewTreeObserver
                        .removeOnGlobalLayoutListener(this)

                val position =


                        roomCanvas!!.setOnTouchListener { v, event ->
                            if (toAddFurniture) {
                                projectViewModel.currentPosition =
                                        Point3D(event.rawX, 0f, event.rawY).toAbsolutLocation(
                                                projectViewModel.room.getRoomRatio(),
                                                projectViewModel.layoutMeasures)
                                projectViewModel.newFurniture = true
                                Navigation.findNavController(v)
                                        .navigate(R.id.action_floorPlanFragment_to_addFurnitureFragment2)
                            }
                            toAddFurniture = false
                            return@setOnTouchListener true
                        }
                addFab.setOnClickListener {
                    Toast.makeText(requireContext(),
                            "Tap where you wish to place a new furniture",
                            Toast.LENGTH_SHORT).show()
                    toAddFurniture = true
                }
            }
        })
        hamburger.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

//
//    hamburger.setOnClickListener {
//        if (! drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.openDrawer(GravityCompat.START)
//            navigationView.bringToFront()
//            navigationView.setNavigationItemSelectedListener(this)
//
//        }
//
//    }
//}

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.share_floor_plan_img) {
//            saveScreenshot(this.requireView())
//        }
//        return true;
//    }
//
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun saveScreenshot(view: View) {
//        val window = (view.context as Activity).window
//        if (window != null) {
//            var bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
//            val locationOfViewInWindow = IntArray(2)
//            view.getLocationInWindow(locationOfViewInWindow)
//            try {
//                PixelCopy.request(window, Rect(locationOfViewInWindow[0], locationOfViewInWindow[1], locationOfViewInWindow[0] + view.width, locationOfViewInWindow[1] + view.height), bitmap, { copyResult ->
//                    if (copyResult == PixelCopy.SUCCESS) {
//
//                        //TODO: finish
////                        val intent = Intent(Intent.ACTION_SEND)
////                        intent.setType("image/png")
////                        intent.putExtra(Intent.EXTRA_STREAM, bitmap)
////                        startActivity(Intent.createChooser(intent, "Share"))
//                    }
//                    // possible to handle other result codes ...
//                }, Handler())
//            } catch (e: IllegalArgumentException) {
//                // PixelCopy may throw IllegalArgumentException, make sure to handle it
//            }
//        }
//    }
//
//}

}
