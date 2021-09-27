package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import postpc.finalproject.RoomInn.R

class Dresser(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(50f, 90f, 150f),
    color: Int = Color.GRAY,
    roomId: String = ""
) : Furniture(position, rotation, scale, color) {
    //copy constructor
    constructor(fur:Dresser) : this(fur.position,fur.rotation,fur.scale,fur.color, fur.roomId){
        id = fur.id
        unityType= fur.unityType
        type=fur.type
        roomId=fur.roomId
        freeScale=fur.freeScale
    }
    init {
        type = "Dresser"
        this.roomId = roomId
        unityType= typeMap[1]!!    }

    companion object {
        val typeMap = mapOf<Int, FurnitureType>(
            1 to FurnitureType("Fashion", R.drawable.dresser1, Point3D(120f,	90f	,50f), "addNewDresserTypeOne",1),
            2 to FurnitureType("Practical", R.drawable.dresser2, Point3D(100f,	85f	,45f), "addNewDresserTypeTwo",2),
            3 to FurnitureType("Night Stand" , R.drawable.dresser3, Point3D(40f	,50f	,45f), "addNewDresserTypeThree",3)
        )

    }
    override fun draw(sizeWidth: Float, sizeHeight: Float): Path {
        val path = Path()
        val margin = 8f
        path.addRoundRect(
            margin,
            margin,
            (scale.x * sizeWidth) - margin,
            (scale.z * sizeHeight) - 2 * margin,
            (scale.x * sizeWidth) / 15,
            (scale.z * sizeHeight) / 15,
            Path.Direction.CCW
        )
        path.addRect(
            (scale.x * sizeWidth * 0.2f),
            (scale.z * sizeHeight) - 2 * margin,
            (scale.x * sizeWidth * 0.35f) ,
            (scale.z * sizeHeight) - margin, Path.Direction.CCW
        )
        path.addRect(
            (scale.x * sizeWidth * 0.65f),
            (scale.z * sizeHeight) - 2 * margin,
            (scale.x * sizeWidth * 0.8f) ,
            (scale.z * sizeHeight) - margin, Path.Direction.CCW
        )
        return path
    }
}