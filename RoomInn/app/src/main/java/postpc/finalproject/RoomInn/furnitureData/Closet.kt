package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import postpc.finalproject.RoomInn.R

class Closet(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(Armchair.typeMap[1]!!.defaultScale),
    color: Int = Color.GRAY,
    roomId: String = ""
) : Furniture(position, rotation, scale, color) {
    //copy constructor
    constructor(fur:Closet) : this(fur.position,fur.rotation,fur.scale,fur.color, fur.roomId){
        id = fur.id
        type=fur.type
        roomId=fur.roomId
        unityType=fur.unityType
        freeScale=fur.freeScale
    }
    init {
        type = "Closet"
        this.roomId = roomId
        unityType = typeMap[1]!!    }

    companion object {
        val typeMap = mapOf<Int, FurnitureType>(
            1 to FurnitureType("Small", R.drawable.closet1, Point3D(   150f,	200f	,50f), "addNewClosetTypeOne",1),
            2 to FurnitureType("Medium", R.drawable.closet2, Point3D(200f	,200f	,70f), "addNewClosetTypeTwo",2),
            3 to FurnitureType("Large" , R.drawable.closet3, Point3D(75f,	200f,	55f), "addNewClosetTypeThree",3)
        )

    }


    override fun draw(sizeWidth:Float, sizeHeight:Float): Path {
        val path = Path()
        val margin = 8f
        path.addRect(
            margin,
            margin,
            (scale.x * sizeWidth) + margin/2,
            ((scale.z * sizeHeight) / 2f) + margin,
            Path.Direction.CCW
        )
        path.moveTo(((scale.x * sizeWidth) + margin) / 2, ((scale.z * sizeHeight) / 2f) + margin)
        path.lineTo(((scale.x * sizeWidth) + margin) / 2, margin)
        path.moveTo(((scale.x * sizeWidth) + margin/2), ((scale.z * sizeHeight) / 2f) + margin)
        path.arcTo(
            ((scale.x * sizeWidth) + margin) / 2,
            ((scale.z * sizeHeight) / 3f),
            (scale.x * sizeWidth) + margin,
            ((scale.z * sizeHeight * 2) / 3f) + margin,
            120f, 50f, false)
        path.moveTo((margin), ((scale.z * sizeHeight) / 2f) + margin)

        path.arcTo(
            (margin) ,
            ((scale.z * sizeHeight) / 3f),
            ((scale.x * sizeWidth) + margin) / 2,
            ((scale.z * sizeHeight * 2) / 3f) + margin,
            60f, -50f, false)
        return path
    }
}