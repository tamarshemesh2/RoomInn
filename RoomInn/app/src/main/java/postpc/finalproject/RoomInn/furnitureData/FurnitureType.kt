package postpc.finalproject.RoomInn.furnitureData

data class FurnitureType(
    var typeName: String = "",
    var typeRecID: Int = 0,
    var defaultScale: Point3D = Point3D(),
    var unityFuncName: String = "",
    val key: Int = 1
) {
}