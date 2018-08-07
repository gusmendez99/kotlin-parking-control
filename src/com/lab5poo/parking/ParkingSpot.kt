package com.lab5poo.parking

class ParkingSpot(
        val id:String,
        var row: Int,
        var column: Int
){

    override fun toString(): String {
        return id
    }
}

