package com.lab5poo.parking

class ParkingSpot(
        val id:String,
        var row: Int,
        var column: Int,
        var isAvailable:Boolean = false
){

    fun changeState(){
        isAvailable != isAvailable
    }

    override fun toString(): String {
        return id
    }

}

