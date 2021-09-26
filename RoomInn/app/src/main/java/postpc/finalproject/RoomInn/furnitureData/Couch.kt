package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import postpc.finalproject.RoomInn.R

class Couch(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(150f, 70f, 65f),
    color: Int = Color.GRAY,
    roomId: String = ""
) : Furniture(position, rotation, scale, color) {
    //copy constructor
    constructor(fur:Couch) : this(fur.position,fur.rotation,fur.scale,fur.color, fur.roomId){
        id = fur.id
        type=fur.type
        roomId=fur.roomId
        unityType = fur.unityType
        freeScale=fur.freeScale
    }
    init {
        type = "Couch"
        this.roomId = roomId
        unityType= typeMap[1]!!    }



    companion object {
        val typeMap = mapOf<Int, FurnitureType>(
            1 to FurnitureType("Two Seats", R.id.save_scale_type, Point3D(   150f	,75f	,70f), "addNewCouchTypeOne",1),
            2 to FurnitureType("L Couch - Left", R.id.save_scale_type, Point3D(185f	,75f	,130f), "addNewCouchTypeTwo",2),
            3 to FurnitureType("L Couch - Right" , R.id.save_scale_type, Point3D(180f	,55f	,130f), "addNewCouchTypeThree",3)
        )

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