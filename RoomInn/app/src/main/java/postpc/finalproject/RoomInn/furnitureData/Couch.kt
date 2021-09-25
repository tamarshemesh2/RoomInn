package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path

class Couch(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(150f, 70f, 65f),
    color: Int = Color.GRAY,
    roomId: String = ""
) : Furniture(position, rotation, scale, color) {
    init {
        unityFuncName = "addNewCouch"
        type = "Couch"
        this.roomId = roomId
        defaultScale = Point3D(scale)
    }


    override fun draw(sizeWidth: Float, sizeHeight: Float): Path {
        val path = Path()
        val margin = 8f
        // back pillows
        path.addRect(
            margin,
            margin,
            ((scale.x * sizeWidth) +0.5f*margin) / 2,
            ((scale.z * sizeHeight * 2) / 5f) - margin,
            Path.Direction.CCW
        )
        path.addRect(
            ((scale.x * sizeWidth) +0.6f*margin) / 2,
            margin,
            (scale.x * sizeWidth) - ( margin),
            ((scale.z * sizeHeight *2) / 5f) - margin,
            Path.Direction.CCW
        )
        // seats pillows
        val sidePillow = scale.x * sizeWidth * 0.08f
        path.addRoundRect(
            sidePillow +margin,
            ((scale.z * sizeHeight * 2) / 5f) - margin,
            ((scale.x * sizeWidth) + margin) / 2,
            ((scale.z * sizeHeight)) - margin,
            (scale.x * sizeWidth).toFloat()/10 ,
            (scale.z * sizeHeight).toFloat()/6,
            Path.Direction.CCW
        )
        path.addRoundRect(
            ((scale.x * sizeWidth) + margin) / 2,
            ((scale.z * sizeHeight * 2) / 5f) - margin,
            (scale.x * sizeWidth) - (sidePillow + margin),
            ((scale.z * sizeHeight)) - margin,
            (scale.x * sizeWidth).toFloat()/10 ,
            (scale.z * sizeHeight).toFloat()/6,
            Path.Direction.CCW
        )
        // hands pillows
        path.addRect(
            margin,
            ((scale.z * sizeHeight *2) / 5f) - margin,
            margin+ sidePillow,
            ((scale.z * sizeHeight)) - margin,
            Path.Direction.CCW
        )
        path.addRect(
            (scale.x * sizeWidth) - (sidePillow +margin),
            ((scale.z * sizeHeight *2) / 5f) - margin,
            (scale.x * sizeWidth) - (margin),
            ((scale.z * sizeHeight)) - margin,
            Path.Direction.CCW
        )
        return path
    }
}