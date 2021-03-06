package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import postpc.finalproject.RoomInn.R

class Table(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(typeMap[1]!!.defaultScale),
    color: Int = Color.GRAY,
    roomId: String = ""
): Furniture(position,rotation, scale, color){
    //copy constructor
    constructor(fur:Table) : this(fur.position,fur.rotation,fur.scale,fur.color, fur.roomId){
        id = fur.id
        unityType=fur.unityType
        type=fur.type
        roomId=fur.roomId
        freeScale=fur.freeScale
    }
    init {
        unityType= typeMap[1]!!
        type = "Table"
        this.roomId = roomId
    }

    companion object {
        val typeMap = mapOf<Int, FurnitureType>(
            1 to FurnitureType("Desk", R.drawable.table1, Point3D(   150f	,75f,	60f), "addNewTableTypeOne",1),
            2 to FurnitureType("Table", R.drawable.table2, Point3D(100f,	80f	,60f), "addNewTableTypeTwo",2),
            3 to FurnitureType("Coffee Table" , R.drawable.table3, Point3D(100f,	30f	,60f), "addNewTableTypeThree",3)
        )

    }

    override fun draw(sizeWidth: Double, sizeHeight: Double): Path {
        val path = Path()
        val margin = 8f
        path.addRect(
            margin,
            margin,
            ((scale.x * sizeWidth) - margin).toFloat(),
            ((scale.z * sizeHeight)-margin).toFloat(),
            Path.Direction.CCW
        )
        return path
    }
}