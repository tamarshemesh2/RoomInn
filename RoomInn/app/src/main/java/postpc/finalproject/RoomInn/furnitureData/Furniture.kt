package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import android.util.Size
import java.lang.Double.min

import java.util.*
import kotlin.math.min

abstract class Furniture(
    var position : Point3D,
    var rotation : Point3D,
    var scale : Point3D,
    var color: Int = Color.GRAY,
    var id: String = UUID.randomUUID().toString()) {

    var type: String = "unknown"
    var roomId: String = "unknown"
    var unityFuncName: String = ""
    var freeScale: Boolean = false

    open fun scale(scaleFactor: Float): Point3D {
        // returns a Point3D item that holds the differ distance between the edges of the furniture from old to new scale
        val oldScale = Point3D(scale)
        val newScale = Point3D(scale.multiply(scaleFactor))
        return newScale.add(oldScale.multiply(-1f)).multiply(1f).apply { this.y = 0f }
    }
    open fun getSizeToDraw(size: Size):Pair<Float,Float>{
        val ratioSize = min(size.height/(scale.z),size.width/scale.x)
        return Pair(ratioSize,ratioSize)
    }
    open fun getOffsetToFit(windowWidth: Int, windowHeight:Int):Pair<Float,Float>{
        val first = getSizeToDraw(Size(windowWidth.toInt(), windowHeight.toInt())).first
        val heightMargin = (windowHeight - (first * scale.z)) / 2
        val widthMargin = (windowWidth - (first * scale.x)) / 2
        return Pair(widthMargin,heightMargin)
    }
    abstract fun draw(sizeWidth:Float, sizeHeight: Float): Path
}
