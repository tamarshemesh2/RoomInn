package postpc.finalproject.RoomInn.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.models.RoomInnApplication



class ProfileFragment : Fragment() {
    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val projectViewModel: ProjectViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ProjectViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // find all views
        val projectRecyclerView: RecyclerView = view.findViewById(R.id.projects_recycler)
        val addProjectFab: FloatingActionButton = view.findViewById(R.id.fab_add)
        val adapter = projectViewModel.adapter
        adapter.setContext(requireContext())

        // add a swipe gesture to delete item
//        val swipeDeleteGesture = object : ProjectAdapterSwipeDeleteGesture() {
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val projectItemHolder = viewHolder as ProjectItemHolder
//                val deleteBtn = projectItemHolder.delButton
//                when (direction) {
//                    ItemTouchHelper.RIGHT -> {
//                        deleteBtn.animate().translationX(-50f)
//                        deleteBtn.visibility = View.VISIBLE
//                        deleteBtn.animate().translationX(0f).setDuration(100)
//                            .alpha(1f).duration = 100
//                        deleteBtn.isClickable = true
//                        deleteBtn.setOnClickListener {
//                            adapter.deleteProject(
//                                viewHolder.adapterPosition
//                            )
//                        }
//                    }
//                    ItemTouchHelper.LEFT -> {
//                        deleteBtn.animate().translationX(-50f).setDuration(100)
//                            .alpha(0f).duration = 100
//                        deleteBtn.visibility = View.GONE
//                        deleteBtn.isClickable = false
//                    }
//                }
//            }
//        }
//
//        val itemTouchHelper = ItemTouchHelper(swipeDeleteGesture)
//        itemTouchHelper.attachToRecyclerView(projectRecyclerView)

        // setup function to call from DB upon change the current rendered room
        RoomInnApplication.getInstance().getRoomsDB().loadRoomNavLambda = {
            Navigation.findNavController(view)
                .navigate(R.id.action_profileFragment2_to_floorPlanFragment)
        }
        // needs to be changed to daniella's unity features
        addProjectFab.setOnClickListener {
            val intent = Intent(requireContext(), UnityHandler::class.java)
            intent.putExtra("Scene Index", ScanUnityPlayerActivity.sceneIndex)
            startActivity(intent)
            projectViewModel.doorsAndWindows.clear()
            Navigation.findNavController(view)
                .navigate(R.id.action_profileFragment2_to_floorPlanPlacingFragment)
        }

        // set the recycle view
        projectRecyclerView.adapter = adapter
        projectRecyclerView.layoutManager =
            GridLayoutManager(requireActivity(), 1, RecyclerView.VERTICAL, false)
        projectRecyclerView.layoutManager
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

}
