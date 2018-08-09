package com.lab5poo.level

import com.lab5poo.car.Car
import com.lab5poo.parking.ParkingSpot
import com.lab5poo.wall.Wall

class Level(
        private val name:String,
        private val id:String,
        private val color:String,
        private var height: Int = 0,
        private var width: Int = 0,
        private val cars: ArrayList<Car> = ArrayList(),
        private val walls: ArrayList<Wall> = ArrayList(),
        private val parkingSpots: ArrayList<ParkingSpot> = ArrayList()
){
    //Getters and setters
    fun getId():String {
        return id
    }

    fun getName():String {
        return name
    }

    fun getColor():String {
        return color
    }

    fun setHeight(value:Int){
        this.height = value
    }

    fun setWidth(value:Int){
        this.width = value
    }


    fun addCar(car:Car):Boolean{ //TODO: complete fun addCar()
        if(hasParkingSpotAt(car.getRow(), car.getColumn())){
            setParkingSpotUnavailable(car.getRow(), car.getColumn())
        }
        cars.add(car)
        return true
    }

    private fun setParkingSpotUnavailable(row: Int, column: Int) {
        parkingSpots.filter { it.getRow() == row && it.getColumn() == column }.forEach { parking -> parking.changeState() }
    }

    fun addWall(wall:Wall):Boolean{
        walls.add(wall)
        return true
    }

    fun addParkingSpot(parkingSpot: ParkingSpot):Boolean {
        if(isValidParkingSpot(parkingSpot.getId()) && !hasRepeatedParkingSpots(parkingSpot.getId())) {
            parkingSpots.add(parkingSpot)
            return true
        }
        return false
    }

    fun hasWallAt(row:Int, column:Int):Boolean{
        return this.walls.any { it.getRow() == row && it.getColumn() == column }
    }

    fun hasCarAt(row:Int, column:Int):Boolean{
        return this.cars.any { it.getRow() == row && it.getColumn() == column }
    }

    fun hasParkingSpotAt(row:Int, column:Int):Boolean{
        return this.parkingSpots.any { it.getRow() == row && it.getColumn() == column && it.isAvailable()}
    }

    fun getParkingSpotAt(row:Int, column:Int):ParkingSpot?{
        return this.parkingSpots.firstOrNull { it.getRow() == row && it.getColumn() == column && it.isAvailable()}
    }

    fun findParkingSpotById(parkingSpotSelected: String): ParkingSpot? {
        return this.parkingSpots.firstOrNull { it.getId() == parkingSpotSelected }
    }

    fun findCarByPlate(plate:String):Car?{
        return this.cars.firstOrNull{ it.getPlate() == plate }
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
        return parkingSpots.any { it -> it.getId() == currentId }
    }

    fun existsCarByPlate(plate:String):Boolean{
        return cars.any { it.getPlate() == plate }
    }

    fun hasAvailableParkingSpots():Boolean {
        return parkingSpots.any {it.isAvailable()}
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
                        parkingLotStr += parkingSpot!!.getId()
                    }
                    else -> parkingLotStr += " "
                }
            }
            parkingLotStr += "\n"
        }
        return parkingLotStr
    }
    
}