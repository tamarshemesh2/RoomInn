package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import android.util.Size
import postpc.finalproject.RoomInn.furnitureData.Point3D
import java.util.*

class Window(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(130f, 60f, 65f),
) : Furniture(position, rotation, scale, Color.BLACK) {
    //copy constructor
    constructor(fur:Window) : this(fur.position,fur.rotation,fur.scale){
        id= fur.id
        defaultScale = fur.defaultScale
        type=fur.type
        roomId=fur.roomId
        unityFuncName=fur.unityFuncName
        freeScale=fur.freeScale
    }
    init{
        type = "Window"
        defaultScale = Point3D(this.scale)
    }

    override fun draw(sizeWidth: Float, sizeHeight: Float): Path {
        val path = Path()
        val margin = 8f
        path.addRect(
            margin,
            margin,
            (scale.x * sizeWidth) + margin,
            (10 * sizeHeight) + margin,
            Path.Direction.CCW
        )
        return path
    }

    fun drawFront(sizeWidth: Float, sizeHeight: Float): Path {
        val path = Path()
        val margin = 8f
        path.addRect(
            margin,
            margin,
            (scale.x * sizeWidth) + margin,
            (scale.z * sizeHeight) + margin,
            Path.Direction.CCW
        )
        return path
    }

}