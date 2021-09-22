package postpc.finalproject.RoomInn.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.furnitureData.*
import postpc.finalproject.RoomInn.ui.furnitureCategoryItem.FurnitureCategoryItemAdapter

class AddFurnitureFragment : Fragment() {
    companion object {
        fun newInstance() = AddFurnitureFragment()
    }

    private val projectViewModel: ProjectViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ProjectViewModel::class.java)
    }

    private val adapter: FurnitureCategoryItemAdapter = FurnitureCategoryItemAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: finish finding all the views
        // find all views
        val furnitureRecyclerView: RecyclerView = view.findViewById(R.id.furniture_recycler)
        val position = projectViewModel.currentPosition

        val defaultItems = mapOf(
            "Bed" to (Bed(position = Point3D(position), roomId = projectViewModel.room.id)),
            "Chair" to (Chair(position = Point3D(position), roomId = projectViewModel.room.id)),
            "Closet" to (Closet(position = Point3D(position), roomId = projectViewModel.room.id)),
            "Desk" to (Desk(position = Point3D(position), roomId = projectViewModel.room.id)),
        )
        defaultItems.forEach {
            it.value.position = it.value.position.add(Point3D(it.value.scale).multiply(-0.5f))
        }
        adapter.setViewModel(projectViewModel)
        adapter.setContext(requireContext())
        adapter.setItems(defaultItems)

        // set the recycle view
        furnitureRecyclerView.adapter = adapter
        furnitureRecyclerView.layoutManager =
            GridLayoutManager(requireActivity(), 2, RecyclerView.VERTICAL, false)
        furnitureRecyclerView.layoutManager
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_furniture, container, false)
    }

}
