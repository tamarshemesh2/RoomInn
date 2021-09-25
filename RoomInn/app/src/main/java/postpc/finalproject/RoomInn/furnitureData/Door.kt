package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import android.util.Size
import java.util.*

class Door(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(80f, 206f, 10f),
    color: Int = Color.BLACK
) : Furniture(position, rotation, scale, Color.BLACK) {
    var orientation:String = "RTL"

    init{
        type = "Door"
        defaultScale = Point3D(scale)
    }


    override fun draw(sizeWidthA: Float, sizeHeight: Float): Path {
        val path = Path()
        val margin = 8f
        val sizeWidth = sizeWidthA
        if (orientation=="RTL"){
            val sizeWidth = -sizeWidthA
        }

        path.moveTo(margin, ((scale.x * sizeHeight) + margin))
        path.arcTo(
            -scale.x * sizeWidth + margin,
            margin,
            scale.x * sizeWidth + margin,
            (scale.x * sizeHeight * 2) + margin,
            -90f, 90f, false
        )

        path.lineTo(margin * 0.5f+((scale.x * sizeWidth)*0.9f), ((scale.x * sizeHeight) + margin))
        path.moveTo(margin * 0.5f+((scale.x * sizeWidth)*0.75f), ((scale.x * sizeHeight) + margin))
        path.lineTo(margin * 0.5f+((scale.x * sizeWidth)*0.6f), ((scale.x * sizeHeight) + margin))
        path.moveTo(margin * 0.5f+((scale.x * sizeWidth)*0.45f), ((scale.x * sizeHeight) + margin))
        path.lineTo(margin * 0.5f+((scale.x * sizeWidth)*0.3f), ((scale.x * sizeHeight) + margin))
        path.moveTo(margin * 0.5f+((scale.x * sizeWidth)*0.15f), ((scale.x * sizeHeight) + margin))
        path.lineTo(margin * 0.5f, ((scale.x * sizeHeight) + margin))

        path.moveTo(margin * 2, ((scale.x * sizeHeight) + margin))
        path.lineTo(margin * 2, margin)
        return path
    }

}