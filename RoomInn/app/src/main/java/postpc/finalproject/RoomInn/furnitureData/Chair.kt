package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import postpc.finalproject.RoomInn.R

class Chair(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(typeMap[1]!!.defaultScale),
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

    override fun draw(sizeWidth: Double, sizeHeight: Double): Path {
        val path = Path()
        val margin = 8f
        val handPillowWidth = (scale.x * sizeWidth) / 9f
        val handPillowHeight = (scale.z * sizeHeight * 5f) / 9f
        //seat
        path.addRoundRect(
            (handPillowWidth + margin).toFloat(),
            (margin),
            (((scale.x * sizeWidth)-handPillowWidth) - margin).toFloat(),
            ((scale.z * sizeHeight )-margin).toFloat(),
            (scale.x * sizeWidth).toFloat() / 5,
            (scale.z * sizeHeight).toFloat() / 5,
            Path.Direction.CCW
        )
        path.addRoundRect(
            (handPillowWidth + margin).toFloat(),
            (handPillowHeight*0.7f).toFloat(),
            (((scale.x * sizeWidth)-handPillowWidth) - margin).toFloat(),
            ((scale.z * sizeHeight )-margin).toFloat(),
            (scale.x * sizeWidth).toFloat() / 5,
            (scale.z * sizeHeight).toFloat() / 5,
            Path.Direction.CCW
        )
        // hands
        path.addRoundRect(
            (margin),
            (handPillowHeight*0.6f).toFloat(),
            (handPillowWidth + margin).toFloat(),
            ((1.6*handPillowHeight) - margin).toFloat(),
            (scale.x * sizeWidth).toFloat() / 11,
            (scale.z * sizeHeight).toFloat() / 11,
            Path.Direction.CCW
        )
        path.addRoundRect(
            (((scale.x * sizeWidth)-handPillowWidth) - margin).toFloat(),
            (handPillowHeight*0.6f).toFloat(),
            (((scale.x * sizeWidth)) - margin).toFloat(),
            ((1.6*handPillowHeight) - margin).toFloat(),
            (scale.x * sizeWidth).toFloat() / 11,
            (scale.z * sizeHeight).toFloat() / 11,
            Path.Direction.CCW
        )
        return path
    }
}