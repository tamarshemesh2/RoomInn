package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path

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
            y = 1.0
        }
    }

}