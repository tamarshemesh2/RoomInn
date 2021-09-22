package postpc.finalproject.RoomInn.ui.furnitureCategoryItem

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import postpc.finalproject.RoomInn.FurnitureCanvas
import postpc.finalproject.RoomInn.R

class FurnitureCategoryItemHolder(view: View) : RecyclerView.ViewHolder(view) {
    val categoryTitle: TextView = view.findViewById(R.id.category_title)
    var categoryImg: FurnitureCanvas = view.findViewById(R.id.category_img)
    val bg: ConstraintLayout = view.findViewById(R.id.bgLayout)

}