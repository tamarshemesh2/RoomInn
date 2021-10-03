package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import postpc.finalproject.RoomInn.R

class Armchair(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(typeMap[1]!!.defaultScale),
    color: Int = Color.GRAY,
    roomId: String = ""
) : Furniture(position, rotation, scale, color) {
    //copy constructor
    constructor(fur: Armchair) : this(fur.position, fur.rotation, fur.scale, fur.color, fur.roomId) {
        id = fur.id
        unityType = fur.unityType
        type = fur.type
        roomId = fur.roomId
        freeScale = fur.freeScale
    }

    init {
        type = "Armchair"
        this.roomId = roomId
        unityType = typeMap[1]!!
    }

    companion object {
        val typeMap = mapOf<Int, FurnitureType>(
            1 to FurnitureType("Classy", R.drawable.armchair1, Point3D(70f,	100f,	80f), "addNewArmchairTypeOne",1),
            2 to FurnitureType("Square", R.drawable.armchair2, Point3D(100f	,95f	,100f), "addNewArmchairTypeTwo",2),
            3 to FurnitureType("IKEA" ,R.drawable.armchair3, Point3D(80f	,140f	,100f), "addNewArmchairTypeThree",3)
        )

    }

    override fun draw(sizeWidth: Double, sizeHeight: Double): Path {
        val path = Path()
        val margin = 8f
        val sidePillow = scale.x * sizeWidth * 0.2f
        // back pillows
        path.addRoundRect(
            margin,
            margin,
            (((scale.x * sizeWidth) -margin).toFloat()),
            (((scale.z * sizeHeight)) - margin).toFloat(),
            (scale.x * sizeWidth).toFloat()/10 ,
            (scale.z * sizeHeight).toFloat()/10,
            Path.Direction.CCW
        )
        path.addRoundRect(
            margin,
            margin,
            (((scale.x * sizeWidth) -margin).toFloat()),
            (((scale.z * sizeHeight *2) / 5f) - margin).toFloat(),
            (scale.x * sizeWidth).toFloat()/5 ,
            (scale.z * sizeHeight).toFloat()/10,
            Path.Direction.CCW
        )

        path.moveTo((margin+ sidePillow).toFloat(),
            (((scale.z * sizeHeight *2) / 5f) - margin+(scale.z * sizeHeight).toFloat()/10).toFloat()
        )
        path.lineTo(((scale.x * sizeWidth) - (sidePillow +margin)).toFloat(),
            (((scale.z * sizeHeight *2) / 5f) - margin+(scale.z * sizeHeight).toFloat()/10).toFloat()
        )

//         hands pillows
        path.addRoundRect(
            margin,
            (((scale.z * sizeHeight *2) / 5f) - margin).toFloat(),
            (margin+ sidePillow).toFloat(),
            (((scale.z * sizeHeight)) - margin).toFloat(),
            (scale.x * sizeWidth).toFloat()/10 ,
            (scale.z * sizeHeight).toFloat()/10,
            Path.Direction.CCW
        )
        path.addRoundRect(
            ((scale.x * sizeWidth) - (sidePillow +margin)).toFloat(),
            (((scale.z * sizeHeight *2) / 5f) - margin).toFloat(),
            ((scale.x * sizeWidth) - (margin)).toFloat(),
            (((scale.z * sizeHeight)) - margin).toFloat(),
            (scale.x * sizeWidth).toFloat()/10 ,
            (scale.z * sizeHeight).toFloat()/10,
            Path.Direction.CCW
        )
        return path}
}