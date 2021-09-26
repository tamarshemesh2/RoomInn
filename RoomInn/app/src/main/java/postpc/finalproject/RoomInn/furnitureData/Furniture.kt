package postpc.finalproject.RoomInn.furnitureData

import android.graphics.Color
import android.graphics.Path
import android.util.Log
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
    var renderType: Int = 0
    //copy constructor
    constructor(fur:Furniture) : this(fur.position,fur.rotation,fur.scale,fur.color, fur.id){
        defaultScale = fur.defaultScale
        type=fur.type
        roomId=fur.roomId
        unityFuncName=fur.unityFuncName
        freeScale=fur.freeScale
    }



    open fun scale(
        scaleFactor: Float,
        newPivot: Point3D = Point3D(position).add(Point3D(scale).multiply(0.5f))
    ): Point3D {
        // returns a Point3D item that holds the differ distance between the edges of the furniture from old to new scale
        val oldScale = Point3D(scale)
        val newScale = Point3D(scale.multiply(scaleFactor))
        val oldPivot = Point3D(position).add(Point3D(scale).multiply(0.5f))
        val distance = newPivot.add(oldPivot.multiply(-1f)) // newPivot-oldPivot
        return newScale.add(oldScale.multiply(-1f)).multiply(0.5f)
            .add(distance.multiply(-1f)).multiply(-1f).apply { this.y = 0f }
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
        Log.e("yuvalScale",scale.toString() )
        Log.e("NatiScale",defaultScale.toString() )
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

    override fun equals(other: Any?): Boolean {
        val o = other as Furniture
        return (this.id == o.id) &&
                (this.position == o.position) &&
                (this.rotation == o.rotation) &&
                (this.scale == o.scale) &&
                (this.color == o.color) &&
                (this.defaultScale == o.defaultScale) &&
                (this.type == o.type) && (
                this.roomId == o.roomId) &&
                (this.unityFuncName == o.unityFuncName) &&
                (this.freeScale == o.freeScale)
    }


    abstract fun draw(sizeWidth: Float, sizeHeight: Float): Path
    override fun hashCode(): Int {
        var result = position.hashCode()
        result = 31 * result + rotation.hashCode()
        result = 31 * result + scale.hashCode()
        result = 31 * result + color
        result = 31 * result + id.hashCode()
        result = 31 * result + defaultScale.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + roomId.hashCode()
        result = 31 * result + unityFuncName.hashCode()
        result = 31 * result + freeScale.hashCode()
        return result
    }


}
