package postpc.finalproject.RoomInn.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.furnitureData.*
import postpc.finalproject.RoomInn.ui.furnitureCategoryItem.FurnitureCategoryItemAdapter
import postpc.finalproject.RoomInn.ui.furnitureTypeItem.FurnitureTypeItemAdapter

class ChooseFurnitureTypeFragment : Fragment() {
    companion object {
        fun newInstance() = ChooseFurnitureTypeFragment()
    }

    private val projectViewModel: ProjectViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ProjectViewModel::class.java)
    }

    private val adapter: FurnitureTypeItemAdapter = FurnitureTypeItemAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: finish finding all the views
        // find all views
        val furnitureRecyclerView: RecyclerView = view.findViewById(R.id.furniture_type_recycler)
        val furnitureType = projectViewModel.furniture!!.type
        //todo :
        // string - type name to show,
        // first int - R.id.img,
        // second int- number to put in renderType in Furniture Object
        val typeMap: Map<Int,FurnitureType>? = (when (furnitureType) {
            ("Chair") -> Chair.typeMap
            ("Bed") -> Bed.typeMap
            ("Closet") -> Closet.typeMap
            ("Couch") -> Couch.typeMap
            ("Table") -> Table.typeMap
            ("Dresser") -> Dresser.typeMap
            else -> null
        })
        if (typeMap == null) {
            Navigation.findNavController(view)
                .navigate(R.id.action_chooseFurnitureTypeFragment_to_editFurnitureFragment)
        } else {

            adapter.setViewModel(projectViewModel) as Map<String, Pair<Int, Int>>
            adapter.setContext(requireContext())
            adapter.setItems(typeMap)

            // set the recycle view
            furnitureRecyclerView.adapter = adapter
            furnitureRecyclerView.layoutManager =
                GridLayoutManager(requireActivity(), 1, RecyclerView.HORIZONTAL, false)
            furnitureRecyclerView.layoutManager
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_type_furniture, container, false)
    }

}
