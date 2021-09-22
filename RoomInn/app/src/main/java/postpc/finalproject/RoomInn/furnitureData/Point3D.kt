package postpc.finalproject.RoomInn.furnitureData

data class Point3D(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f
) {

    constructor(copy: Point3D) : this() {
        this.x = copy.x
        this.y = copy.y
        this.z = copy.z
    }

    fun getRelativeLocation(roomRatio: Float, roomLocation: IntArray): Point3D {
        return Point3D((x * roomRatio) + roomLocation[0], y, (z * roomRatio) + roomLocation[1])

    }

    fun toAbsolutLocation(roomRatio: Float, roomLocation: IntArray) :Point3D{
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
        x *= scaleFactor
        y *= scaleFactor
        z *= scaleFactor

        return this
    }


}