package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import android.util.Log
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.models.RoomInnApplication

class Door(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(typeMap[1]!!.defaultScale),
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
                "Left",
                R.drawable.door1,
                Point3D(90f, 210f, 15f),
                "addNewDoor",
                1
            ),
            2 to FurnitureType(
                "Right",
                R.drawable.door2,
                Point3D(90f, 210f, 15f),
                "addNewDoor",
                2
            ),
        )

    }

    init {
        type = "Door"
        unityType = FurnitureType("door", defaultScale = Point3D(scale), unityFuncName = "")
    }


    override fun draw(sizeWidth: Double, sizeHeight: Double): Path {
        val path = Path()
        val margin = 8f
        if (unityType.typeName == "Right") {
            path.moveTo(margin, (((scale.x * sizeHeight) + margin).toFloat()))
            path.arcTo(
                (-scale.x * sizeWidth + margin).toFloat(),
                margin,
                (scale.x * sizeWidth + margin).toFloat(),
                ((scale.x * sizeHeight * 2) + margin).toFloat(),
                -90f, 90f, false
            )

            path.lineTo(
                (margin * 0.5f + ((scale.x * sizeWidth) * 0.9f)).toFloat(),
                (((scale.x * sizeHeight) + margin).toFloat())
            )
            path.moveTo(
                (margin * 0.5f + ((scale.x * sizeWidth) * 0.75f)).toFloat(),
                (((scale.x * sizeHeight) + margin).toFloat())
            )
            path.lineTo(
                (margin * 0.5f + ((scale.x * sizeWidth) * 0.6f)).toFloat(),
                (((scale.x * sizeHeight) + margin).toFloat())
            )
            path.moveTo(
                (margin * 0.5f + ((scale.x * sizeWidth) * 0.45f)).toFloat(),
                (((scale.x * sizeHeight) + margin).toFloat())
            )
            path.lineTo(
                (margin * 0.5f + ((scale.x * sizeWidth) * 0.3f)).toFloat(),
                (((scale.x * sizeHeight) + margin).toFloat())
            )
            path.moveTo(
                (margin * 0.5f + ((scale.x * sizeWidth) * 0.15f)).toFloat(),
                (((scale.x * sizeHeight) + margin).toFloat())
            )
            path.lineTo(margin * 0.5f, (((scale.x * sizeHeight) + margin).toFloat()))

            path.moveTo(margin * 2, (((scale.x * sizeHeight) + margin).toFloat()))
            path.lineTo(margin * 2, margin)
        } else {
            path.moveTo(margin, margin)

            path.arcTo(
                ((-scale.x * sizeWidth + margin).toFloat()),
                ((-scale.x * sizeHeight + margin).toFloat()),
                ((scale.x * sizeWidth + margin).toFloat()),
                ((scale.x * sizeHeight + margin).toFloat()),
                90f, -90f, false
            )
            path.lineTo(
                (margin * 0.5f + ((scale.x * sizeWidth) * 0.9f)).toFloat(),
                (margin)
            )
            path.moveTo(
                (margin * 0.5f + ((scale.x * sizeWidth) * 0.75f)).toFloat(),
                (margin)
            )
            path.lineTo(
                (margin * 0.5f + ((scale.x * sizeWidth) * 0.6f)).toFloat(),
                (margin)
            )
            path.moveTo(
                (margin * 0.5f + ((scale.x * sizeWidth) * 0.45f)).toFloat(),
                (margin)
            )
            path.lineTo(
                (margin * 0.5f + ((scale.x * sizeWidth) * 0.3f)).toFloat(),
                (margin)
            )
            path.moveTo(
                (margin * 0.5f + ((scale.x * sizeWidth) * 0.15f)).toFloat(),
                (margin)
            )
            path.lineTo(margin * 0.5f, (margin))

            path.moveTo(margin * 2, (((scale.x * sizeHeight) + margin).toFloat()))
            path.lineTo(margin * 2, margin)
        }
        return path
    }

    override fun unityScale(): Point3D {
        return scale.getDivideByPoint(unityType.defaultScale).apply {
            if (z > 1) {
                z = 1.0
            }
        }
    }

    override fun unityPosition(): Point3D {
        val screenPosition = Point3D(position).add(Point3D(0.0, 0.0, (scale.x) * 0.5))
        val doorCenter = Point3D(position).add(Point3D((scale.x) * 0.5, 0.0, (scale.x) * 0.5))
        screenPosition.rotateAroundPointByAngle(doorCenter, (rotation.y.toFloat()) % 360)

        val roomCenter = Point3D(
            RoomInnApplication.getInstance().getRoomsDB().roomByRoomID(roomId).roomCenterGetter()
        )

        return screenPosition.add(roomCenter.multiply(-1f))
            .getDivideByPoint(Point3D(100f, 100f, -100f)).apply { y = 0.0 }
    }

    override fun toString(): String {
        return unityPosition().toString() + "\n" +
                Point3D(rotation).add(Point3D(0f, 90f, 0f)).toString() + "\n" +
                unityScale().toString() + "\n" +
                color.toString()
    }

}