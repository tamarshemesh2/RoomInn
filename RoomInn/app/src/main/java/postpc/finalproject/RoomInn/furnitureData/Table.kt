package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path

class Table(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(150f, 120f, 60f),
    color: Int = Color.GRAY,
    roomId: String = ""
): Furniture(position,rotation, scale, color){
    //copy constructor
    constructor(fur:Table) : this(fur.position,fur.rotation,fur.scale,fur.color, fur.roomId){
        id = fur.id
        defaultScale = fur.defaultScale
        type=fur.type
        roomId=fur.roomId
        unityFuncName=fur.unityFuncName
        freeScale=fur.freeScale
    }
    init {
        unityFuncName = "addNewTable"
        type = "Table"
        this.roomId = roomId
        defaultScale = Point3D(scale)
    }

    override fun draw(sizeWidth:Float, sizeHeight:Float): Path {
        val path = Path()
        val margin = 8f
        path.addRect(
            margin,
            margin,
            (scale.x * sizeWidth) - margin,
            (scale.z * sizeHeight)-margin,
            Path.Direction.CCW
        )
        return path
    }
}