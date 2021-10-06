package postpc.finalproject.RoomInn.ui.furnitureTypeItem

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import postpc.finalproject.RoomInn.R

class FurnitureTypeItemHolder(view: View) : RecyclerView.ViewHolder(view) {
    val typeTitle: TextView = view.findViewById(R.id.type_title)
    var typeImg: ImageView = view.findViewById(R.id.type_img)
    val bg: ConstraintLayout = view.findViewById(R.id.type_bg)

}