package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import postpc.finalproject.RoomInn.R

class Couch(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(Armchair.typeMap[2]!!.defaultScale),
    color: Int = Color.GRAY,
    roomId: String = ""
) : Furniture(position, rotation, scale, color) {
    //copy constructor
    constructor(fur: Couch) : this(fur.position, fur.rotation, fur.scale, fur.color, fur.roomId) {
        id = fur.id
        type = fur.type
        roomId = fur.roomId
        unityType = fur.unityType
        freeScale = fur.freeScale
    }

    init {
        type = "Couch"
        this.roomId = roomId
        unityType = typeMap[2]!!
    }


    companion object {
        val typeMap = mapOf<Int, FurnitureType>(
            1 to FurnitureType(
                "Two Seats",
                R.drawable.couch1,
                Point3D(190f, 85f, 80f),
                "addNewCouchTypeOne",
                1
            ),
            2 to FurnitureType(
                "L Couch - Left",
                R.drawable.couch2,
                Point3D(185f, 75f, 130f),
                "addNewCouchTypeTwo",
                2
            ),
            3 to FurnitureType(
                "L Couch - Right",
                R.drawable.couch3,
                Point3D(180f, 55f, 130f),
                "addNewCouchTypeThree",
                3
            )
        )

    }


    override fun draw(sizeWidth: Float, sizeHeight: Float): Path {
        val path = Path()
        val margin = 8f
        val sidePillow = scale.x * sizeWidth * 0.08f
        val backPillow = scale.z * sizeHeight * 0.2f



        when (unityType.typeName) {
            "L Couch - Left" -> {
                // back pillow
                path.addRect(
                    margin,
                    margin,
                    (scale.x * sizeWidth) - (margin),
                    backPillow,
                    Path.Direction.CCW
                )
                //seat pillows
                path.addRoundRect(
                    sidePillow + margin,
                    backPillow + margin,
                    ((scale.x * sizeWidth) + margin) * 0.6f,
                    (((scale.z * sizeHeight) - margin)) * 0.6f,
                    (scale.x * sizeWidth).toFloat() / 14,
                    (scale.z * sizeHeight).toFloat() / 14,
                    Path.Direction.CCW
                )

                path.addRoundRect(
                    ((scale.x * sizeWidth) + margin) * 0.6f,
                    backPillow + margin,
                    (scale.x * sizeWidth) - (sidePillow + margin),
                    (((scale.z * sizeHeight)) - margin),
                    (scale.x * sizeWidth).toFloat() / 14,
                    (scale.z * sizeHeight).toFloat() / 14,
                    Path.Direction.CCW
                )
                // hands pillows
                path.addRect(
                    margin,
                    backPillow + margin,
                    margin + sidePillow,
                    (((scale.z * sizeHeight)) - margin) * 0.6f,
                    Path.Direction.CCW
                )
                path.addRect(
                    (scale.x * sizeWidth) - (sidePillow + margin),
                    backPillow + margin,
                    (scale.x * sizeWidth) - (margin),
                    (((scale.z * sizeHeight)) - margin),
                    Path.Direction.CCW
                )
            }
            "L Couch - Right" -> {
                // back pillow
                path.addRect(
                    margin,
                    margin,
                    (scale.x * sizeWidth) - (margin),
                    backPillow,
                    Path.Direction.CCW
                )
                //seat pillows
                path.addRoundRect(
                    sidePillow + margin,
                    backPillow + margin,
                    ((scale.x * sizeWidth) + margin) * 0.35f,
                    (((scale.z * sizeHeight)) - margin),
                    (scale.x * sizeWidth).toFloat() / 14,
                    (scale.z * sizeHeight).toFloat() / 14,
                    Path.Direction.CCW
                )

                path.addRoundRect(
                    ((scale.x * sizeWidth) + margin) * 0.35f,
                    backPillow + margin,
                    (scale.x * sizeWidth) - (sidePillow + margin),
                    (((scale.z * sizeHeight) - margin)) * 0.6f,
                    (scale.x * sizeWidth).toFloat() / 14,
                    (scale.z * sizeHeight).toFloat() / 14,
                    Path.Direction.CCW
                )
                // hands pillows
                path.addRect(
                    margin,
                    backPillow + margin,
                    margin + sidePillow,
                    (((scale.z * sizeHeight)) - margin),
                    Path.Direction.CCW
                )
                path.addRect(
                    (scale.x * sizeWidth) - (sidePillow + margin),
                    backPillow + margin,
                    (scale.x * sizeWidth) - (margin),
                    (((scale.z * sizeHeight)) - margin) * 0.6f,
                    Path.Direction.CCW
                )
            }
            else -> {
                // back pillow
                path.addRect(
                    margin,
                    margin,
                    ((scale.x * sizeWidth) + 0.5f * margin) / 2,
                    backPillow,
                    Path.Direction.CCW
                )
                path.addRect(
                    ((scale.x * sizeWidth) + 0.6f * margin) / 2,
                    margin,
                    (scale.x * sizeWidth) - (margin),
                    backPillow,
                    Path.Direction.CCW
                )
                // seat pillows
                path.addRoundRect(
                    ((scale.x * sizeWidth) + margin) / 2,
                    backPillow + margin,
                    (scale.x * sizeWidth) - (sidePillow + margin),
                    ((scale.z * sizeHeight)) - margin,
                    (scale.x * sizeWidth).toFloat() / 14,
                    (scale.z * sizeHeight).toFloat() / 14,
                    Path.Direction.CCW
                )
                path.addRoundRect(
                    margin + sidePillow,
                    backPillow + margin,
                    ((scale.x * sizeWidth) + margin) / 2,
                    ((scale.z * sizeHeight)) - margin,
                    (scale.x * sizeWidth).toFloat() / 14,
                    (scale.z * sizeHeight).toFloat() / 14,
                    Path.Direction.CCW
                )
                // hands pillows
                path.addRect(
                    margin,
                    backPillow + margin,
                    margin + sidePillow,
                    ((scale.z * sizeHeight)) - margin,
                    Path.Direction.CCW
                )
                path.addRect(
                    (scale.x * sizeWidth) - (sidePillow + margin),
                    backPillow + margin,
                    (scale.x * sizeWidth) - (margin),
                    ((scale.z * sizeHeight)) - margin,
                    Path.Direction.CCW
                )
            }
        }
        return path
    }
}