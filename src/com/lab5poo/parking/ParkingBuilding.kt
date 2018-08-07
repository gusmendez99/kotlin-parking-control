package com.lab5poo.parking

import com.lab5poo.level.Level

class ParkingBuilding(
        val levels:ArrayList<Level> = ArrayList()
){
    fun addLevel(level: Level):Boolean {
        if(!existsLevel(level)){
            levels.add(level)
            return true
        }
        return false
    }

    fun deleteLevel(id:String):Boolean{
        val level:Level? = getLevelById(id)
        if(level != null){
            levels.remove(level)
            return true
        }
        return false
    }

    private fun existsLevel(level:Level):Boolean {
        return levels.any {
            it ->  it.color == level.color &&
                it.id == level.id &&
                it.name == level.name
        }
    }

    private fun getLevelById(id: String): Level? {
        return levels.firstOrNull { it -> it.id == id }
    }

    override fun toString(): String {
        var levelsToStr = ""
        levels.forEach {
            levelsToStr += "LEVEL ID: $it.id, NAME: $it.name, COLOR: $it.color\n"
            levelsToStr += "$it.parkingLot\n"
        }
        return levelsToStr
    }

    fun findCarByPlate(plate:String): Boolean {
        levels.forEach {
            if(it.parkingLot.existsCarByPlate(plate)) return true
        }
        return false
    }

    fun getLevelsWithAvailableSpaces(): List<Level> {
        return levels.filter { it.parkingLot.hasAvailableParkingSpots() }
    }

    fun getAvailableLevels():String {
        var levelStr = ""
        getLevelsWithAvailableSpaces()
                .forEachIndexed { index, level -> levelStr += "${index + 1}: ${level.name} (ID: $level.id)\n" }
        return """
            NIVELES DISPONIBLES:
                $levelStr
        """.trimIndent()

    }

    fun getLevelByCarPlate(plate: String): Level? {
        return levels.firstOrNull { level -> level.parkingLot.existsCarByPlate(plate) }
    }


}