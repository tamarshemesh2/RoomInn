package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path

class Dresser(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(50f, 90f, 150f),
    color: Int = Color.GRAY,
    roomId: String = ""
) : Furniture(position, rotation, scale, color) {
    init {
        unityFuncName = "addNewDresser"
        type = "Dresser"
        this.roomId = roomId
        defaultScale = Point3D(scale)
    }

    override fun draw(sizeWidth: Float, sizeHeight: Float): Path {
        val path = Path()
        val margin = 8f
        path.addRoundRect(
            margin,
            margin,
            (scale.x * sizeWidth) - margin,
            (scale.z * sizeHeight) - 2 * margin,
            (scale.x * sizeWidth) / 15,
            (scale.z * sizeHeight) / 15,
            Path.Direction.CCW
        )
        path.addRect(
            (scale.x * sizeWidth * 0.2f),
            (scale.z * sizeHeight) - 2 * margin,
            (scale.x * sizeWidth * 0.35f) ,
            (scale.z * sizeHeight) - margin, Path.Direction.CCW
        )
        path.addRect(
            (scale.x * sizeWidth * 0.65f),
            (scale.z * sizeHeight) - 2 * margin,
            (scale.x * sizeWidth * 0.8f) ,
            (scale.z * sizeHeight) - margin, Path.Direction.CCW
        )
        return path
    }
}