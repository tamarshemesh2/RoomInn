package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path

class Chair(
    position: Point3D = Point3D(),
    rotation: Point3D = Point3D(),
    scale: Point3D = Point3D(42f, 96f, 52f),
    color: Int = Color.GRAY,
    roomId: String = ""
): Furniture(position,rotation, scale, color){
    init {
        unityFuncName = "addNewChair"
        type = "Chair"
        this.roomId = roomId
    }

    override fun draw(sizeWidth:Float, sizeHeight:Float): Path {
        val path = Path()
        val margin = 8f
        //seat
        path.addRoundRect(
            ((scale.x * sizeWidth*2)/9)+margin,
            (scale.z * sizeHeight*2)/9 ,
            ((scale.x *7* sizeWidth)/9)+margin,
            (scale.z * sizeHeight),
            (scale.x * sizeWidth).toFloat()/4 ,
            (scale.z * sizeHeight).toFloat()/4,
            Path.Direction.CCW
        )
        path.addRoundRect(
            ((scale.x * sizeWidth*2)/9)+margin,
            (scale.z * sizeHeight*4)/9 ,
            ((scale.x *7* sizeWidth)/9)+margin,
            (scale.z * sizeHeight) ,
            (scale.x *sizeWidth).toFloat()/4 ,
            (scale.z * sizeHeight).toFloat()/4,
            Path.Direction.CCW
        )
        // hands
        path.addRoundRect(
            ((scale.x * sizeWidth)/9)+margin,
            (scale.z * sizeHeight*4)/9 ,
            ((scale.x * sizeWidth*2)/9)+margin,
            (scale.z * sizeHeight) - margin ,
            (scale.x * sizeWidth).toFloat()/10 ,
            (scale.z * sizeHeight).toFloat()/10,
            Path.Direction.CCW
        )
        path.addRoundRect(
            ((scale.x *7* sizeWidth)/9)+margin,
            (scale.z * sizeHeight*4)/9,
            ((scale.x *8* sizeWidth)/9)+margin,
            (scale.z * sizeHeight) - margin ,
            (scale.x * sizeWidth).toFloat()/10 ,
            (scale.z * sizeHeight).toFloat()/10,
            Path.Direction.CCW
        )
               return path
    }
}