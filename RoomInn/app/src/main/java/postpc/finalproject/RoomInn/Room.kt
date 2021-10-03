package postpc.finalproject.RoomInn

import android.graphics.Path
import android.os.Build
import android.util.Size
import androidx.annotation.RequiresApi
import postpc.finalproject.RoomInn.furnitureData.Wall
import postpc.finalproject.RoomInn.furnitureData.Door
import postpc.finalproject.RoomInn.furnitureData.Point3D
import postpc.finalproject.RoomInn.furnitureData.Window
import java.util.*


@RequiresApi(Build.VERSION_CODES.N)
data class Room(
    var Corners: MutableList<Point3D> = mutableListOf(),
    var Walls: MutableList<Wall> = mutableListOf(),
    var windows: MutableList<Window> = mutableListOf(),
    var doors: MutableList<Door> = mutableListOf(),
    private var displayRatio: Double = 1.0,
    var name: String = "Project Name",
    var userId: String = "user id"
) {
    private var minX = 0.0
    private var minZ = 0.0
    private val roomCenter: Point3D by lazy {
        init()
    }
    var isInit = false
    val id = UUID.randomUUID().toString()


    fun init(): Point3D {
        if (Corners.size == 0)
            return Point3D()
        val fCorner = Corners.first()
        minX = fCorner.x
        minZ = fCorner.z
        var maxX = fCorner.x
        var maxZ = fCorner.z
        for (corner in Corners) {
            if (corner.x > maxX) {
                maxX = corner.x
            } else if (corner.x < minX) {
                minX = corner.x
            }
            if (corner.z < minZ) {
                minZ = corner.z
            } else if (corner.z > maxZ) {
                maxZ = corner.z
            }
        }
        val normalPoint = Point3D(-minX, 0.0, -minZ)
        val roomCenter = Point3D((maxX + minX) / 2f, 0.0, (maxZ + minZ) / 2f)
        Corners.replaceAll { it.add(normalPoint) }
        roomCenter.add(normalPoint)
        isInit = true
        return roomCenter
    }

    fun mirrorCorners(): MutableList<Point3D> {
        val mirrorCorners = mutableListOf<Point3D>()
        var maxX = Corners.first().x
        Corners.forEach {
            if (it.x > maxX) {
                maxX = it.x
            }
            mirrorCorners.add(Point3D(it).apply { x *= -1 })
        }
        mirrorCorners.forEach { it.add(Point3D(maxX,0.0,0.0)) }
        return mirrorCorners
    }

    fun rotateRoomCornersByAngle(angle: Float, center: Point3D = roomCenterGetter()) {
        for (corner in Corners) {
            corner.rotateAroundPointByAngle(center, angle)
        }
        init()
    }

    fun roomCenterGetter(): Point3D {
        if (!isInit) {
            init()
        }
        return roomCenter!!
    }

    private fun getRoomSize(): Size {
        if (!isInit) {
            init()
        }
        if (Corners.size == 0)
            return Size(0, 0)
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

    fun getOffsetToFit(windowWidth: Int, windowHeight: Int): Pair<Double, Double> {
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
            (boardSize.width - 20) / roomSize.width.toDouble(),
            boardSize.height / roomSize.height.toDouble()
        )
    }

    fun getRoomRatio(): Double {
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
            path.moveTo(((last.x) * displayRatio).toFloat(), ((last.z) * displayRatio).toFloat())
            for (point in Corners) {
                path.lineTo(((point.x) * displayRatio).toFloat(),
                    ((point.z) * displayRatio).toFloat()
                )
                path.moveTo(((point.x) * displayRatio).toFloat(),
                    ((point.z) * displayRatio).toFloat()
                )
            }
        }
        return path
    }
}
