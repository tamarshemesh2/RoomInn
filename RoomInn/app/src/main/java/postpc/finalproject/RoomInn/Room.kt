package postpc.finalproject.RoomInn

import android.graphics.Path
import android.os.Build
import android.util.Log
import android.util.Size
import androidx.annotation.RequiresApi
import com.postpc.myapplication.furnitureData.Wall
import postpc.finalproject.RoomInn.furnitureData.Door
import postpc.finalproject.RoomInn.furnitureData.Furniture
import postpc.finalproject.RoomInn.furnitureData.Point3D
import postpc.finalproject.RoomInn.furnitureData.Window
import java.util.*


@RequiresApi(Build.VERSION_CODES.N)
data class Room(
    var Corners: MutableList<Point3D> = mutableListOf(),
    var Walls: MutableList<Wall> = mutableListOf(),
    var windows: MutableList<Window> = mutableListOf(),
    var doors: MutableList<Door> = mutableListOf(),
    private var displayRatio: Float = 1f,
    var name: String = "Project Name",
    var userId: String = "user id"
) {
    private var minX = 0f
    private var minZ = 0f
    var isInit = false
    val id = UUID.randomUUID().toString()


    fun init() {
        for (corner in Corners) {
            if (corner.x < minX) {
                minX = corner.x
            }
            if (corner.z < minZ) {
                minZ = corner.z
            }
        }
        val normalPoint = Point3D(-minX, 0f, -minZ)
        Corners.replaceAll { it.add(normalPoint) }
        isInit = true
    }

    fun getRoomCenter(): Point3D {
        if (!isInit) {
            init()
        }
        val center = Point3D()
        for (corner in Corners) {
            center.add(corner)
        }
        return center.multiply(1f / Corners.size)
    }

    private fun getRoomSize(): Size {
        if (!isInit) {
            init()
        }
        var maxX = Corners.first().x
        var maxZ = Corners.first().z
        for (corner in Corners) {
            if (corner.x > maxX) {
                maxX = corner.x
            }
            if (corner.z > maxZ) {
                maxZ = corner.z
            }
        }
        return Size((maxX).toInt(), (maxZ).toInt())
    }

    fun getOffsetToFit(windowWidth: Int, windowHeight: Int): Pair<Float, Float> {
        if (!isInit) {
            init()
        }
        val roomSize = getRoomSize()
        val heightMargin = (windowHeight - (roomSize.height * displayRatio)) / 2
        val widthMargin = (windowWidth - (roomSize.width * displayRatio)) / 2
        return Pair(widthMargin, heightMargin)
    }

    fun setSizeRoomRatio(boardSize: Size) {
        if (!isInit) {
            init()
        }
        val roomSize = getRoomSize()
        displayRatio = minOf(
            (boardSize.width - 30) / roomSize.width.toFloat(),
            boardSize.height / roomSize.height.toFloat()
        )
    }

    fun getRoomRatio(): Float {
        // must have a call to set room ratio before!
        if (!isInit) {
            init()
        }
        return displayRatio
    }

    fun drawFloorPlan(boardWidth: Int = 0, boardHeight: Int = 0): Path {
        if (!isInit) {
            init()
        }
        val path = Path()
        if (boardWidth != 0) {
            setSizeRoomRatio(Size(boardWidth, boardHeight))
        }
        if (Corners.size > 0) {
            val last = Corners.last()
            path.moveTo((last.x) * displayRatio, (last.z) * displayRatio)
            for (point in Corners) {
                path.lineTo((point.x) * displayRatio, (point.z) * displayRatio)
                path.moveTo((point.x) * displayRatio, (point.z) * displayRatio)
            }
        }
        return path
    }
}
