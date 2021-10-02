package postpc.finalproject.RoomInn.furnitureData

import postpc.finalproject.RoomInn.furnitureData.Point3D
import postpc.finalproject.RoomInn.models.RoomInnApplication

data class Wall(var position : Point3D = Point3D(),
                var rotation : Point3D = Point3D(),
                var scale : Point3D = Point3D(),
                var roomId :String = "unknown"
)
{
    override fun toString() : String {
        return  unityPosition().toString() + "\n" +
                rotation.toString() + "\n" +
                scale.toString()
    }

    private fun unityPosition(): Point3D {
        val screenPosition = Point3D(position)
        val roomCenter = Point3D(
            RoomInnApplication.getInstance()
                .getRoomsDB().roomByRoomID(roomId)
                .roomCenterGetter())

        return (screenPosition.add(roomCenter.multiply(-1f)))
            .getDivideByPoint(Point3D(100f, 100f, -100f)).apply { y = 0f }
    }
}