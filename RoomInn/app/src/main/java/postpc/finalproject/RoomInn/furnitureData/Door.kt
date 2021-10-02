package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import android.util.Size
import postpc.finalproject.RoomInn.R
import java.util.*

class Door(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(Door.typeMap[2]!!.defaultScale),
    color: Int = Color.BLACK
) : Furniture(position, rotation, scale, Color.BLACK) {
    //copy constructor
    constructor(fur: Door) : this(fur.position, fur.rotation, fur.scale, fur.color) {
        id = fur.id
        type = fur.type
        roomId = fur.roomId
        unityType = fur.unityType
        freeScale = fur.freeScale
    }

    companion object {
        val typeMap = mapOf<Int, FurnitureType>(
            1 to FurnitureType(
                "Right",
                R.drawable.armchair1,
                Point3D(90f, 100f, 210f),
                "addNewArmchairTypeOne",
                1
            ),
            2 to FurnitureType(
                "Left",
                R.drawable.armchair2,
                Point3D(90f, 100f, 210f),
                "addNewArmchairTypeTwo",
                2
            ),
        )

    }

    init {
        type = "Door"
        unityType = FurnitureType("door", defaultScale = Point3D(scale), unityFuncName = "")
    }


    override fun draw(sizeWidth: Float, sizeHeight: Float): Path {
        val path = Path()
        val margin = 8f
        if (unityType.typeName == "Right") {
            path.moveTo(margin, ((scale.x * sizeHeight) + margin))
            path.arcTo(
                -scale.x * sizeWidth + margin,
                margin,
                scale.x * sizeWidth + margin,
                (scale.x * sizeHeight * 2) + margin,
                -90f, 90f, false
            )

            path.lineTo(
                margin * 0.5f + ((scale.x * sizeWidth) * 0.9f),
                ((scale.x * sizeHeight) + margin)
            )
            path.moveTo(
                margin * 0.5f + ((scale.x * sizeWidth) * 0.75f),
                ((scale.x * sizeHeight) + margin)
            )
            path.lineTo(
                margin * 0.5f + ((scale.x * sizeWidth) * 0.6f),
                ((scale.x * sizeHeight) + margin)
            )
            path.moveTo(
                margin * 0.5f + ((scale.x * sizeWidth) * 0.45f),
                ((scale.x * sizeHeight) + margin)
            )
            path.lineTo(
                margin * 0.5f + ((scale.x * sizeWidth) * 0.3f),
                ((scale.x * sizeHeight) + margin)
            )
            path.moveTo(
                margin * 0.5f + ((scale.x * sizeWidth) * 0.15f),
                ((scale.x * sizeHeight) + margin)
            )
            path.lineTo(margin * 0.5f, ((scale.x * sizeHeight) + margin))

            path.moveTo(margin * 2, ((scale.x * sizeHeight) + margin))
            path.lineTo(margin * 2, margin)
        } else {
            path.moveTo(margin , margin)

            path.arcTo(
                (-scale.x * sizeWidth+margin),
                (-scale.x * sizeHeight+margin),
                (scale.x * sizeWidth+margin),
                (scale.x * sizeHeight+margin),
                90f, -90f, false
            )
            path.lineTo(
                margin * 0.5f + ((scale.x * sizeWidth) * 0.9f),
                (margin)
            )
            path.moveTo(
                margin * 0.5f + ((scale.x * sizeWidth) * 0.75f),
                (margin)
            )
            path.lineTo(
                margin * 0.5f + ((scale.x * sizeWidth) * 0.6f),
                (margin)
            )
            path.moveTo(
                margin * 0.5f + ((scale.x * sizeWidth) * 0.45f),
                (margin)
            )
            path.lineTo(
                margin * 0.5f + ((scale.x * sizeWidth) * 0.3f),
                (margin)
            )
            path.moveTo(
                margin * 0.5f + ((scale.x * sizeWidth) * 0.15f),
                (margin)
            )
            path.lineTo(margin * 0.5f, (margin))

            path.moveTo(margin * 2, ((scale.x * sizeHeight) + margin))
            path.lineTo(margin * 2, margin)
        }
    return path
}

    override fun unityScale(): Point3D {
        return scale.getDivideByPoint(unityType.defaultScale).apply {
            z = 1f
        }
    }

    override fun toString() : String {
        return  unityPosition().toString() + "\n" + // TODO - Change back to unityPosition
                Point3D(rotation).add(Point3D(0f,-90f,0f)).toString() + "\n" +
                scale.toString() + "\n" +
                color.toString()
    }

}