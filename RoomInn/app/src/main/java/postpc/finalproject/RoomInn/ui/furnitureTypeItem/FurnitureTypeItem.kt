package postpc.finalproject.RoomInn.ui.furnitureTypeItem

import postpc.finalproject.RoomInn.furnitureData.Furniture
import postpc.finalproject.RoomInn.ui.furnitureCategoryItem.FurnitureCategoryItem

//import com.google.firebase.Timestamp


data class FurnitureTypeItem(
    val furnitureTypeName: String,
    val furnitureTypeImg: Int,
    val furnitureRenderType: Int
) : Comparable<FurnitureTypeItem> {
    override fun compareTo(other: FurnitureTypeItem): Int {
        return furnitureTypeName.compareTo(other.furnitureTypeName)
    }
}