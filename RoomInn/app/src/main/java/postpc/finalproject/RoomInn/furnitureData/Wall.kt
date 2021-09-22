package com.postpc.myapplication.furnitureData

import postpc.finalproject.RoomInn.furnitureData.Point3D

data class Wall(val position : Point3D,
                val rotation : Point3D,
                val scale : Point3D
)
{

    override fun toString(): String {
        return position.toString() + "\n" +
                rotation.toString() +"\n" +
                scale.toString()
    }
}