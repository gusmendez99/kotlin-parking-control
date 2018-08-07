package com.lab5poo.parking

import com.lab5poo.car.Car
import com.lab5poo.wall.Wall

class ParkingLot(
        var height: Int = 0,
        var width: Int = 0,
        private val cars: ArrayList<Car> = ArrayList(),
        private val walls: ArrayList<Wall> = ArrayList(),
        private val parkingSpots: ArrayList<ParkingSpot> = ArrayList()
){
    fun addCar(car:Car):Boolean{ //TODO: complete fun addCar()
        if(hasParkingSpotAt(car.row, car.column)){
            setParkingSpotUnavailable(car.row, car.column)
        }
        cars.add(car)
        return true
    }

    private fun setParkingSpotUnavailable(row: Int, column: Int) {
        parkingSpots.filter { it.row == row && it.column == column }.forEach { parking -> parking.changeState() }
    }

    fun addWall(wall:Wall):Boolean{
        walls.add(wall)
        return true
    }

    fun addParkingSpot(parkingSpot: ParkingSpot):Boolean {
        if(isValidParkingSpot(parkingSpot.id) && !hasRepeatedParkingSpots(parkingSpot.id)) {
            parkingSpots.add(parkingSpot)
            return true
        }
        return false
    }

    fun hasWallAt(row:Int, column:Int):Boolean{
        return this.walls.any { it.row == row && it.column == column }
    }

    fun hasCarAt(row:Int, column:Int):Boolean{
        return this.cars.any { it.row == row && it.column == column }
    }

    fun hasParkingSpotAt(row:Int, column:Int):Boolean{
        return this.parkingSpots.any { it.row == row && it.column == column && it.isAvailable}
    }

    fun getParkingSpotAt(row:Int, column:Int):ParkingSpot?{
        return this.parkingSpots.firstOrNull { it.row == row && it.column == column && it.isAvailable}
    }

    fun findParkingSpotById(parkingSpotSelected: String): ParkingSpot? {
        return this.parkingSpots.firstOrNull { it.id == parkingSpotSelected }
    }

    fun findCarByPlate(plate:String):Car?{
        return this.cars.firstOrNull{ it.plate == plate }
    }


    //CHECKS PARKING SPOTS
    private fun isValidParkingSpot(item:String): Boolean {
        return if(item.toIntOrNull() != null) {
            val itemInt = item.toInt()
            itemInt in 0..9
        } else {
            item in "a".."z" || item in "A".."Z"
        }
    }

    private fun hasRepeatedParkingSpots(currentId:String): Boolean {
        return parkingSpots.any { it -> it.id == currentId }
    }

    fun existsCarByPlate(plate:String):Boolean{
        return cars.any { it.plate == plate }
    }

    fun hasAvailableParkingSpots():Boolean {
        return parkingSpots.any {it.isAvailable}
    }

    override fun toString(): String {
        var parkingLotStr = ""
        for(row in 0 until height){
            for(column in 0 until width){
                when {
                    hasWallAt(row, column) -> {
                        parkingLotStr += "*"
                    }
                    hasCarAt(row, column) -> {
                        parkingLotStr += "@"
                    }
                    hasParkingSpotAt(row, column) -> {
                        val parkingSpot = getParkingSpotAt(row, column)
                        parkingLotStr += parkingSpot!!.id
                    }
                    else -> parkingLotStr += " "
                }
            }
            parkingLotStr += "\n"
        }
        return parkingLotStr
    }


}