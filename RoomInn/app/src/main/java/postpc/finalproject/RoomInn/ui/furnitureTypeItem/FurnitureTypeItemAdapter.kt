package postpc.finalproject.RoomInn.ui.furnitureTypeItem

import android.content.Context
import android.util.Size
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.furnitureData.Furniture
import postpc.finalproject.RoomInn.furnitureData.FurnitureType
import postpc.finalproject.RoomInn.ui.furnitureCategoryItem.FurnitureCategoryItem
import postpc.finalproject.RoomInn.ui.furnitureCategoryItem.FurnitureCategoryItemHolder
import kotlin.math.roundToInt


class FurnitureTypeItemAdapter : RecyclerView.Adapter<FurnitureTypeItemHolder>() {

    private val _types: MutableList<FurnitureTypeItem> = ArrayList()
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var context: Context

    fun setViewModel(vm: ProjectViewModel) {
        projectViewModel = vm
    }

    fun setContext(cxt: Context) {
        context = cxt
    }

    fun setItems(items: Map<Int, FurnitureType>) {
        _types.clear()
        items.forEach { _types.add(FurnitureTypeItem(it.value)) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FurnitureTypeItemHolder {
        val context = parent.context
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_type_furniture, parent, false)
        return FurnitureTypeItemHolder(view)
    }

    override fun onBindViewHolder(holder: FurnitureTypeItemHolder, position: Int) {
        val furnitureType = _types[position].furnitureType

        holder.typeImg.setImageResource(furnitureType.typeRecID)
        holder.typeTitle.text = furnitureType.typeName

        holder.bg.setOnClickListener {
            projectViewModel.furniture!!.unityType = furnitureType
            projectViewModel.furniture!!.scale = furnitureType.defaultScale
            Navigation.findNavController(it)
                .navigate(R.id.action_chooseFurnitureTypeFragment_to_editFurnitureFragment)
        }
    }

    override fun getItemCount(): Int {
        return _types.size
    }

}