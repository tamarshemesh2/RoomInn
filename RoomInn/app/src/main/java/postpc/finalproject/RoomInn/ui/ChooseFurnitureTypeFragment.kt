package postpc.finalproject.RoomInn.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.furnitureData.*
import postpc.finalproject.RoomInn.ui.furnitureTypeItem.FurnitureTypeItemAdapter
import www.sanju.zoomrecyclerlayout.ZoomRecyclerLayout

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

    @SuppressLint("UseCompatLoadingForDrawables", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // find all views
        val furnitureRecyclerView: RecyclerView = view.findViewById(R.id.furniture_type_recycler)
        val swipeGif: ImageView = view.findViewById(R.id.swipe_img)

        val furnitureType = projectViewModel.furniture!!.type

        val typeMap: Map<Int, FurnitureType>? = (when (furnitureType) {
            ("Chair") -> Chair.typeMap
            ("Armchair") -> Armchair.typeMap
            ("Bed") -> Bed.typeMap
            ("Closet") -> Closet.typeMap
            ("Couch") -> Couch.typeMap
            ("Table") -> Table.typeMap
            ("Dresser") -> Dresser.typeMap
            ("Door") -> Door.typeMap
            else -> null
        })
        if (typeMap == null) {
            Navigation.findNavController(view)
                .navigate(R.id.action_chooseFurnitureTypeFragment_to_editFurnitureFragment)
        } else {

            adapter.setViewModel(projectViewModel)
            adapter.setContext(requireContext())
            adapter.setItems(typeMap)

            swipeHorizontal(swipeGif)



            val linearLayoutManager = ZoomRecyclerLayout(requireContext())
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            linearLayoutManager.reverseLayout = true
            linearLayoutManager.stackFromEnd = true
            furnitureRecyclerView.layoutManager = linearLayoutManager

            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(furnitureRecyclerView)
            furnitureRecyclerView.isNestedScrollingEnabled = false

            // set the recycle view
            furnitureRecyclerView.adapter = adapter
        }
    }

    private fun swipeHorizontal(view: View, moveX: Float = 150F, moveY: Float = 0F) {
        view.animate().alpha(1f).start()
        val translate = TranslateAnimation(0F, moveX, 0F, moveY)
        translate.duration = 2400
        translate.startOffset = 0
        translate.repeatMode = Animation.REVERSE
        translate.interpolator = CycleInterpolator(2F)
        view.startAnimation(translate).also { view.animate().alpha(0f).setDuration(1200).setStartDelay(900).start() }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_type_furniture, container, false)
    }

}
