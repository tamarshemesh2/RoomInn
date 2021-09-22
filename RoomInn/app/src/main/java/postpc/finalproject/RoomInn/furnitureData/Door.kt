package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import android.util.Size
import java.util.*

class Door(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(80f, 206f, 80f),
    color: Int = Color.BLACK
) : Furniture(position, rotation, scale, Color.BLACK) {
    var orientation:String = "RTL"
    init{
        type = "Door"
    }
    override fun draw(sizeWidthA: Float, sizeHeight: Float): Path {
        val path = Path()
        val margin = 8f
        val sizeWidth = sizeWidthA
        if (orientation=="RTL"){
            val sizeWidth = -sizeWidthA
        }

        path.moveTo(margin, ((scale.z * sizeHeight) + margin))
        path.arcTo(
            -scale.x * sizeWidth + margin,
            margin,
            scale.x * sizeWidth + margin,
            (scale.z * sizeHeight * 2) + margin,
            -90f, 90f, false
        )

        path.lineTo(margin * 0.5f+((scale.x * sizeWidth)*0.9f), ((scale.z * sizeHeight) + margin))
        path.moveTo(margin * 0.5f+((scale.x * sizeWidth)*0.75f), ((scale.z * sizeHeight) + margin))
        path.lineTo(margin * 0.5f+((scale.x * sizeWidth)*0.6f), ((scale.z * sizeHeight) + margin))
        path.moveTo(margin * 0.5f+((scale.x * sizeWidth)*0.45f), ((scale.z * sizeHeight) + margin))
        path.lineTo(margin * 0.5f+((scale.x * sizeWidth)*0.3f), ((scale.z * sizeHeight) + margin))
        path.moveTo(margin * 0.5f+((scale.x * sizeWidth)*0.15f), ((scale.z * sizeHeight) + margin))
        path.lineTo(margin * 0.5f, ((scale.z * sizeHeight) + margin))

        path.moveTo(margin * 2, ((scale.z * sizeHeight) + margin))
        path.lineTo(margin * 2, margin)
        return path
    }

}