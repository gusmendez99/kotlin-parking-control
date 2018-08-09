package com.lab5poo.parking

import com.lab5poo.level.Level

class ParkingBuilding(
        private val levels:ArrayList<Level> = ArrayList()
){
    fun getLevels():ArrayList<Level> {
        return levels
    }

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
            it ->  it.getColor() == level.getColor() &&
                it.getId() == level.getId() &&
                it.getName() == level.getName()
        }
    }

    private fun getLevelById(id: String): Level? {
        return levels.firstOrNull { it -> it.getId() == id }
    }

    fun findCarByPlate(plate:String): Boolean {
        levels.forEach {
            if(it.existsCarByPlate(plate)) return true
        }
        return false
    }

    fun getLevelsWithAvailableSpaces(): List<Level> {
        return levels.filter { it.hasAvailableParkingSpots() }
    }

    fun getAvailableLevels():String {
        var levelStr = ""
        getLevelsWithAvailableSpaces()
                .forEachIndexed { index, level -> levelStr += "${index + 1}. ${level.getName()} (ID: ${level.getId()})\n" }

        return """NIVELES DISPONIBLES:
            $levelStr
            """.trimIndent()

    }

    fun getLevelByCarPlate(plate: String): Level? {
        return levels.firstOrNull { level -> level.existsCarByPlate(plate) }
    }

    override fun toString(): String {
        var levelsToStr = ""
        levels.forEach {
            levelsToStr += "NIVEL ID: ${it.getId()}, NOMBRE: ${it.getName()}, COLOR: ${it.getColor()}\n"
            levelsToStr += "$it\n"
        }
        return levelsToStr
    }


}