package com.lab5poo.wall

const val WALL_CHARACTER = "*"

class Wall(
        private val row: Int,
        private val column: Int
){
    fun getRow():Int {
        return row
    }

    fun getColumn():Int {
        return column
    }

    override fun toString(): String {
        return WALL_CHARACTER
    }
}