package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import android.util.Size
import postpc.finalproject.RoomInn.models.RoomInnApplication

import java.util.*
import kotlin.math.min

abstract class Furniture(
    var position: Point3D,
    var rotation: Point3D,
    var scale: Point3D,
    var color: Int = Color.GRAY,
    var id: String = UUID.randomUUID().toString()
) {
    var defaultScale = Point3D();
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

    open fun getSizeToDraw(size: Size): Pair<Float, Float> {
        val ratioSize = min(size.height / (scale.z), size.width / scale.x)
        return Pair(ratioSize, ratioSize)
    }

    open fun getOffsetToFit(windowWidth: Int, windowHeight: Int): Pair<Float, Float> {
        val first = getSizeToDraw(Size(windowWidth.toInt(), windowHeight.toInt())).first
        val heightMargin = (windowHeight - (first * scale.z)) / 2
        val widthMargin = (windowWidth - (first * scale.x)) / 2
        return Pair(widthMargin, heightMargin)
    }

    override fun toString(): String {
        return unityPosition().toString() + "\n" +
                rotation.toString() + "\n" +
                unityScale().toString() + "\n" +
                color.toString()
    }

    open fun unityScale(): Point3D {
        return scale.getDivideByPoint(defaultScale)
    }

    open fun unityPosition(): Point3D {
        val screenPosition = Point3D(position)
        screenPosition.add(scale.getDivideByPoint(Point3D(2f, 2f, 2f)))
        val roomCenter = Point3D(
            RoomInnApplication.getInstance()
                .getRoomsDB().roomByRoomID(roomId)
                .getRoomCenter()
        )
        return screenPosition.add(roomCenter.multiply(-1f))
            .getDivideByPoint(Point3D(100f, 100f, 100f)).apply { y = 0f }
    }


    abstract fun draw(sizeWidth: Float, sizeHeight: Float): Path


}
