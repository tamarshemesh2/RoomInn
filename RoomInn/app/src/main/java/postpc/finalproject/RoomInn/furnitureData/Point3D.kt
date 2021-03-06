package postpc.finalproject.RoomInn.furnitureData

import kotlin.math.cos
import kotlin.math.sin

data class Point3D(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var z: Double = 0.0
) {
    constructor(x: Float, y: Float, z: Float) : this(x.toDouble(), y.toDouble(), z.toDouble())

    constructor(copy: Point3D) : this() {
        this.x = copy.x
        this.y = copy.y
        this.z = copy.z
    }

    fun getRelativeLocation(roomRatio: Double, roomLocation: IntArray): Point3D {
        return Point3D((x * roomRatio) + roomLocation[0], y, (z * roomRatio) + roomLocation[1])
    }

    fun toAbsolutLocation(roomRatio: Double, roomLocation: IntArray): Point3D {
        x = (x - roomLocation[0]) / roomRatio
        z = (z - roomLocation[1]) / roomRatio
        return this
    }

    fun add(step: Point3D): Point3D {
        x += step.x
        y += step.y
        z += step.z
        return this
    }

    fun multiply(scaleFactor: Float): Point3D {
        return multiply(scaleFactor.toDouble())
    }

    fun multiply(scaleFactor: Double): Point3D {
        x *= scaleFactor
        y *= scaleFactor
        z *= scaleFactor
        return this
    }


    fun getDivideByPoint(point: Point3D): Point3D {
        return Point3D(
            x / point.x,
            y / point.y,
            z / point.z
        )
    }

    fun rotateAroundPointByRadAngle(center: Point3D, angle: Float) {

        val x1 = x - center.x;
        val z1 = z - center.z;

        val x2 = x1 * cos(angle) - z1 * sin(angle)
        val z2 = x1 * sin(angle) + z1 * cos(angle)

        x = x2 + center.x;
        z = z2 + center.z;
    }

    override fun toString(): String {
        return "(${String.format("%.4f", x)},${String.format("%.4f", y)},${
            String.format(
                "%.4f",
                z
            )
        })"
    }

    override fun equals(other: Any?): Boolean {
        val o = other as Point3D
        return (x == o.x) && (y == o.y) && (z == o.z)
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }


}