package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import postpc.finalproject.RoomInn.models.RoomInnApplication

class Window(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(130f, 60f, 65f),
) : Furniture(position, rotation, scale, Color.BLACK) {
    //copy constructor
    constructor(fur:Window) : this(fur.position,fur.rotation,fur.scale){
        id= fur.id
        unityType=fur.unityType
        type=fur.type
        roomId=fur.roomId
        freeScale=fur.freeScale
    }
    init{
        type = "Window"
        unityType= FurnitureType(defaultScale = Point3D(scale))    }

    override fun draw(sizeWidth: Double, sizeHeight: Double): Path {
        val path = Path()
        val margin = 8f
        path.addRect(
            margin,
            margin,
            ((scale.x * sizeWidth) + margin).toFloat(),
            ((10 * sizeHeight) + margin).toFloat(),
            Path.Direction.CCW
        )
        return path
    }

    fun drawFront(sizeWidth: Double, sizeHeight: Double): Path {
        val path = Path()
        val margin = 8f
        path.addRect(
            margin,
            margin,
            ((scale.x * sizeWidth) + margin).toFloat(),
            ((scale.z * sizeHeight) + margin).toFloat(),
            Path.Direction.CCW
        )
        return path
    }

    override fun unityScale(): Point3D {
        return scale.getDivideByPoint(unityType.defaultScale).apply {
            y = 0.03
        }
    }

    override fun unityPosition(): Point3D {
        val screenPosition = Point3D(position)
        val roomCenter = Point3D(
            RoomInnApplication.getInstance()
                .getRoomsDB().roomByRoomID(roomId)
                .roomCenterGetter()
        )
        return screenPosition.add(roomCenter.multiply(-1f))
            .getDivideByPoint(Point3D(100f, 100f, -100f))
    }

    override fun toString() : String {
        return  unityPosition().toString() + "\n" +
                Point3D(rotation).add(Point3D(0f,90f,0f)).toString() + "\n" +
                unityScale().toString() + "\n" +
                color.toString()
    }

}