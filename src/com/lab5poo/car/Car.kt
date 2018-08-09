package com.lab5poo.car

class Car (
        private val plate: String,
        private val row: Int,
        private val column: Int
){
    fun getPlate():String {
        return plate
    }

    fun getRow():Int {
        return row
    }

    fun getColumn():Int {
        return column
    }

    override fun toString(): String {
        return "@"
    }


}