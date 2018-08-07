package com.lab5poo.wall

const val WALL_CHARACTER = "*"

class Wall(
        val row: Int,
        val column: Int
){
    override fun toString(): String {
        return WALL_CHARACTER
    }
}