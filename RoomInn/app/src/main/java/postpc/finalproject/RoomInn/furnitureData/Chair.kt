package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import postpc.finalproject.RoomInn.R

class Chair(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(Armchair.typeMap[1]!!.defaultScale),
    color: Int = Color.GRAY,
    roomId: String = ""
) : Furniture(position, rotation, scale, color) {
    //copy constructor
    constructor(fur: Chair) : this(fur.position, fur.rotation, fur.scale, fur.color, fur.roomId) {
        id = fur.id
        type = fur.type
        roomId = fur.roomId
        unityType=fur.unityType
        freeScale = fur.freeScale
    }

    init {
        type = "Chair"
        this.roomId = roomId
        unityType= typeMap[1]!!
    }

    companion object {
        val typeMap = mapOf<Int, FurnitureType>(
            1 to FurnitureType("Executive", R.drawable.chair1, Point3D(75f	,110f	,75f), "addNewChairTypeOne",1),
            2 to FurnitureType("Standard", R.drawable.chair2, Point3D(50f	,105f	,55f), "addNewChairTypeTwo",2),
            3 to FurnitureType("Urban" , R.drawable.chair3, Point3D(60f,	90f	,70f), "addNewChairTypeThree",3)
        )

    }

    override fun draw(sizeWidth: Float, sizeHeight: Float): Path {
        val path = Path()
        val margin = 8f
        //seat
        path.addRoundRect(
            ((scale.x * sizeWidth * 2) / 9) + margin,
            (scale.z * sizeHeight * 1) / 9,
            ((scale.x * sizeWidth * 8) / 9) - margin,
            (scale.z * sizeHeight * 8) / 9,
            (scale.x * sizeWidth).toFloat() / 5,
            (scale.z * sizeHeight).toFloat() / 5,
            Path.Direction.CCW
        )
        path.addRoundRect(
            ((scale.x * sizeWidth * 2) / 9) + margin,
            (scale.z * sizeHeight * 3.5f) / 9,
            ((scale.x * sizeWidth * 8) / 9) - margin,
            (scale.z * sizeHeight * 8) / 9,
            (scale.x * sizeWidth).toFloat() / 5,
            (scale.z * sizeHeight).toFloat() / 5,
            Path.Direction.CCW
        )
        // hands
        path.addRoundRect(
            ((scale.x * sizeWidth) / 9) + margin,
            (scale.z * sizeHeight * 4) / 9,
            ((scale.x * sizeWidth * 2) / 9) + margin,
            ((scale.z * sizeHeight * 8) / 9) - margin,
            (scale.x * sizeWidth).toFloat() / 11,
            (scale.z * sizeHeight).toFloat() / 11,
            Path.Direction.CCW
        )
        path.addRoundRect(
            ((scale.x * sizeWidth * 8) / 9) - margin,
            (scale.z * sizeHeight * 4) / 9,
            ((scale.x * sizeWidth)) - margin,
            ((scale.z * sizeHeight * 8) / 9) - margin,
            (scale.x * sizeWidth).toFloat() / 11,
            (scale.z * sizeHeight).toFloat() / 11,
            Path.Direction.CCW
        )
        return path
    }
}