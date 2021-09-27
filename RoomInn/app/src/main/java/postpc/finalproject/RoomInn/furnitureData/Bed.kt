package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import postpc.finalproject.RoomInn.R
import java.util.*


class Bed(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(Armchair.typeMap[1]!!.defaultScale),
    color: Int = Color.GRAY,
    roomId: String = ""
) : Furniture(position, rotation, scale, color) {
    //copy constructor
    constructor(fur:Bed) : this(fur.position,fur.rotation,fur.scale,fur.color, fur.roomId){
        id = fur.id
        unityType = fur.unityType
        type=fur.type
        roomId=fur.roomId
        freeScale=fur.freeScale
    }
    init {
        type = "Bed"
        this.roomId = roomId
        unityType = typeMap[1]!!    }


    companion object {
        val typeMap = mapOf<Int, FurnitureType>(
            1 to FurnitureType("Standard", R.drawable.bed1, Point3D(   150f	,50f	,200f), "addNewBedTypeOne",1),
            2 to FurnitureType("Fancy", R.drawable.bed2, Point3D(150f	,140f	,220f), "addNewBedTypeTwo",2),
            3 to FurnitureType("Single" , R.drawable.bed3, Point3D(100f,	100f	,180f), "addNewBedTypeThree",3)
        )

    }

    override fun draw(sizeWidth:Float, sizeHeight:Float): Path {
        val path = Path()
        val margin = 8f
        path.addRoundRect(
            margin,
            margin,
            (scale.x * sizeWidth) + margin,
            (scale.z * sizeHeight) + margin,
            (scale.x * sizeWidth).toFloat() / 15,
            (scale.z * sizeHeight).toFloat() / 15,
            Path.Direction.CCW
        )
        if (unityType.typeName=="Single"){
            path.addRoundRect(
                margin,
                3*margin,
                ((scale.x * sizeWidth) + margin),
                (scale.z * sizeHeight + margin) / 2.5f,
                (scale.x * sizeWidth)/ 5,
                (scale.z * sizeHeight) / 5,
                Path.Direction.CCW
            )}else{

        path.addRoundRect(
            margin,
            3*margin,
            ((scale.x * sizeWidth / 2f) + margin),
            (scale.z * sizeHeight + margin) / 2.5f,
            (scale.x * sizeWidth)/ 5,
            (scale.z * sizeHeight) / 5,
            Path.Direction.CCW
        )
        path.addRoundRect(
            ((scale.x * sizeHeight) / 2) + margin,
            3*margin,
            ((scale.x * sizeWidth) + margin),
            (scale.z * sizeHeight + margin) / 2.5f,
            (scale.x * sizeWidth) / 5,
            (scale.z * sizeHeight) / 5,
            Path.Direction.CCW
        )}
        path.moveTo(margin,(scale.z * sizeHeight + margin) / 2f)
        path.lineTo(((scale.x * sizeWidth) + margin),(scale.z * sizeHeight + margin) / 2f)
        return path
    }
}

