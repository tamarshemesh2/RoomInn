package postpc.finalproject.RoomInn.ui.furnitureCategoryItem

import postpc.finalproject.RoomInn.furnitureData.Furniture

data class FurnitureCategoryItem(
    val furnitureCategory: String,
    val furnitureClass: Furniture
) : Comparable<FurnitureCategoryItem> {
    override fun compareTo(other: FurnitureCategoryItem): Int {
        return furnitureCategory.compareTo(other.furnitureCategory)
    }
}