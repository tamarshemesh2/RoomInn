package com.postpc.myapplication.furnitureData

import postpc.finalproject.RoomInn.furnitureData.Point3D

data class Wall(var position : Point3D = Point3D(),
                var rotation : Point3D = Point3D(),
                var scale : Point3D = Point3D()
)
{

    override fun toString(): String {
        return position.toString() + "\n" +
                rotation.toString() +"\n" +
                scale.toString()
    }
}