package postpc.finalproject.RoomInn.ui.furnitureCategoryItem

import android.graphics.Path
import postpc.finalproject.RoomInn.furnitureData.Furniture

//import com.google.firebase.Timestamp


data class FurnitureCategoryItem(
    val furnitureCategory: String,
    val furnitureClass: Furniture
) : Comparable<FurnitureCategoryItem> {
    override fun compareTo(other: FurnitureCategoryItem): Int {
        return furnitureCategory.compareTo(other.furnitureCategory)
    }
}