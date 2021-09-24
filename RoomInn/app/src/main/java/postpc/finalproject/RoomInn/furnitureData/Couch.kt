package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path

class Couch(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(150f, 100f, 65f),
    color: Int = Color.GRAY,
    roomId: String = ""
) : Furniture(position, rotation, scale, color) {
    init {
        unityFuncName = "addNewCouch"
        type = "Closet"
        this.roomId = roomId
        defaultScale = Point3D(scale)
    }


    override fun draw(sizeWidth:Float, sizeHeight:Float): Path {
        val path = Path()
        val margin = 8f
        path.addRect(
            margin,
            margin,
            (scale.x * sizeWidth) + margin/2,
            ((scale.z * sizeHeight) / 2f) + margin,
            Path.Direction.CCW
        )
        path.moveTo(((scale.x * sizeWidth) + margin) / 2, ((scale.z * sizeHeight) / 2f) + margin)
        path.lineTo(((scale.x * sizeWidth) + margin) / 2, margin)
        path.moveTo(((scale.x * sizeWidth) + margin/2), ((scale.z * sizeHeight) / 2f) + margin)
        path.arcTo(
            ((scale.x * sizeWidth) + margin) / 2,
            ((scale.z * sizeHeight) / 3f),
            (scale.x * sizeWidth) + margin,
            ((scale.z * sizeHeight * 2) / 3f) + margin,
            120f, 50f, false)
        path.moveTo((margin), ((scale.z * sizeHeight) / 2f) + margin)

        path.arcTo(
            (margin) ,
            ((scale.z * sizeHeight) / 3f),
            ((scale.x * sizeWidth) + margin) / 2,
            ((scale.z * sizeHeight * 2) / 3f) + margin,
            60f, -50f, false)
        return path
    }
}