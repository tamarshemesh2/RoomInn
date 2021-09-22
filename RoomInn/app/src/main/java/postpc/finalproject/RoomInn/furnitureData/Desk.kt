package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path

class Desk(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(150f, 120f, 60f),
    color: Int = Color.GRAY,
    roomId: String = ""
): Furniture(position,rotation, scale, color){
    init {
        unityFuncName = "addNewDesk"
        type = "Desk"
        this.roomId = roomId    }

    override fun draw(sizeWidth:Float, sizeHeight:Float): Path {
        val path = Path()
        val margin = 8f
        path.addRect(
            margin,
            margin,
            (scale.x * sizeWidth) + margin,
            (scale.z * sizeHeight)+margin,
            Path.Direction.CCW
        )
        return path
    }
}