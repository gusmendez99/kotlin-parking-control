package com.lab5poo.parking

class ParkingSpot(
        private val id:String,
        private val row: Int,
        private val column: Int,
        private var isAvailable:Boolean = true
){

    fun getId():String{
        return id
    }

    fun getRow():Int {
        return row
    }

    fun getColumn():Int {
        return column
    }

    fun isAvailable():Boolean {
        return isAvailable
    }

    fun changeState(){
        isAvailable != isAvailable
    }

    override fun toString(): String {
        return id
    }

}

