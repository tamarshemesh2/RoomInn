package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import postpc.finalproject.RoomInn.R

class Closet(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(150f, 200f, 50f),
    color: Int = Color.GRAY,
    roomId: String = ""
) : Furniture(position, rotation, scale, color) {
    //copy constructor
    constructor(fur:Closet) : this(fur.position,fur.rotation,fur.scale,fur.color, fur.roomId){
        id = fur.id
        defaultScale = fur.defaultScale
        type=fur.type
        roomId=fur.roomId
        unityFuncName=fur.unityFuncName
        freeScale=fur.freeScale
    }
    init {
        unityFuncName = "addNewCloset"
        type = "Closet"
        this.roomId = roomId
        defaultScale = Point3D(scale)
    }

    companion object {
        val typeMap = mapOf<Int, FurnitureType>(
            1 to FurnitureType("Small", R.id.save_scale_type, Point3D(   150f,	200f	,50f), "addNewClosetTypeOne"),
            2 to FurnitureType("Medium", R.id.save_scale_type, Point3D(200f	,200f	,70f), "addNewClosetTypeTwo"),
            3 to FurnitureType("Large" , R.id.save_scale_type, Point3D(75f,	200f,	55f), "addNewClosetTypeThree")
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