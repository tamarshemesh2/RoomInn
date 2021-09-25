package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path

class Chair(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(75f, 110f, 75f),
    color: Int = Color.GRAY,
    roomId: String = ""
): Furniture(position,rotation, scale, color){
    init {
        unityFuncName = "addNewChair"
        type = "Chair"
        this.roomId = roomId
        defaultScale = Point3D(scale)
    }

    override fun draw(sizeWidth:Float, sizeHeight:Float): Path {
        val path = Path()
        val margin = 8f
        //seat
        path.addRoundRect(
            ((scale.x * sizeWidth*2)/9)+margin,
            (scale.z * sizeHeight*1)/9 ,
            ((scale.x * sizeWidth*8)/9)-margin,
            (scale.z * sizeHeight*8)/9,
            (scale.x * sizeWidth).toFloat()/5 ,
            (scale.z * sizeHeight).toFloat()/5,
            Path.Direction.CCW
        )
        path.addRoundRect(
            ((scale.x * sizeWidth*2)/9)+margin,
            (scale.z * sizeHeight*4.2f)/9 ,
            ((scale.x * sizeWidth*8)/9)-margin,
            (scale.z * sizeHeight*8)/9,
            (scale.x *sizeWidth).toFloat()/5 ,
            (scale.z * sizeHeight).toFloat()/5,
            Path.Direction.CCW
        )
        // hands
        path.addRoundRect(
            ((scale.x * sizeWidth)/9)+margin,
            (scale.z * sizeHeight*4)/9 ,
            ((scale.x * sizeWidth*2)/9)+margin,
            ((scale.z * sizeHeight*8)/9) - margin ,
            (scale.x * sizeWidth).toFloat()/11 ,
            (scale.z * sizeHeight).toFloat()/11,
            Path.Direction.CCW
        )
        path.addRoundRect(
            ((scale.x * sizeWidth*8)/9)-margin,
            (scale.z * sizeHeight*4)/9,
            ((scale.x * sizeWidth))-margin,
            ((scale.z * sizeHeight*8)/9) - margin ,
            (scale.x * sizeWidth).toFloat()/11 ,
            (scale.z * sizeHeight).toFloat()/11,
            Path.Direction.CCW
        )
               return path
    }
}