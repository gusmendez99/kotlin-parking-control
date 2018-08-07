package com.lab5poo

import com.lab5poo.car.Car
import com.lab5poo.level.Level
import com.lab5poo.parking.ParkingBuilding
import com.lab5poo.parking.ParkingLot
import com.lab5poo.parking.ParkingSpot
import com.lab5poo.wall.WALL_CHARACTER
import com.lab5poo.wall.Wall
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

const val MENU_MAIN = 0
const val MENU_ADMIN = 1
const val MENU_DRIVER = 2

fun getMenu(type:Int):String {
    when(type) {
        MENU_ADMIN ->
            return """
                Menu:
                    1. Crear nivel
                    2. Eliminar nivel
                    3. Ver todos los niveles
                    4. Salir
            """.trimIndent()

        MENU_DRIVER ->
            return """
                Menu:
                    1. Ingresar placa
                    2. Salir
            """.trimIndent()
        else ->
            return """
                Menu:
                    1. Administrador
                    2. Conductor
                    3. Salir
            """.trimIndent()
    }
}

fun main(args: Array<String>) {
    val building:ParkingBuilding = ParkingBuilding()
    var wantContinues:Boolean = true
    var wantContinuesAdmin:Boolean = true
    var wantContinuesDriver:Boolean = true

    do {
        println(getMenu(MENU_MAIN))
        print("Ingrese una opcion: ")
        val strOption = readLine()!!
        val option = strOption.toInt()
        when(option){
            MENU_ADMIN -> { //MENU: ADMIN
                do {
                    println(getMenu(MENU_ADMIN))
                    print("Ingrese una opcion: ")
                    val strOption = readLine()!!
                    val option = strOption.toInt()
                    when(option){
                        1 -> { //Add level
                            print("Ingrese el nombre: ")
                            val name: String = readLine()!!
                            print("Ingrese el identificador: ")
                            val id: String = readLine()!!
                            print("Ingrese el color: ")
                            val color: String = readLine()!!
                            print("Seleccione el archivo de estructura: ")
                            val path:String? = readFilePath() //TODO: Check if a FileDialog has been opened

                            val parkingLot:ParkingLot? = generateParkingLot(path)
                            if(building.addLevel(Level(name, id, color, parkingLot!!))){
                                println("NIVEL $id AGREGADO EXISTOSAMENTE")
                            } else {
                                println("Error al crear nivel, intenta de nuevo...")
                            }
                        }
                        2 -> { //Delete level
                            print("Ingrese el id: ")
                            val currentId: String = readLine()!!
                            if(building.deleteLevel(currentId)) {
                                println("NIVEL $currentId ELIMINADO EXISTOSAMENTE")
                            } else {
                                println("Error al eliminar nivel, intenta de nuevo...")
                            }

                        }
                        3 -> { // Print all levels
                            println(building)
                        }
                        4 -> {
                            wantContinuesAdmin = false
                        }
                    }
                } while (wantContinuesAdmin)
            }
            MENU_DRIVER -> { //MENU: DRIVER
                do {
                    println(getMenu(MENU_DRIVER))
                    print("Ingrese una opcion: ")
                    val strOption = readLine()!!
                    val option = strOption.toInt()
                    when(option){
                        1 -> { //Plate Input
                            print("Ingrese la placa del carro: ")
                            val plate = readLine()!!
                            if(!building.findCarByPlate(plate)){ //Car Register
                                if(building.getAvailableLevels().isNotEmpty()){
                                    println(building.getAvailableLevels())
                                    print("Ingrese un nivel : ")
                                    val levelStr = readLine()!!
                                    val levelSelected = levelStr.toInt()
                                    val currentLevel = building.getLevelsWithAvailableSpaces()[levelSelected - 1]
                                    println(currentLevel.parkingLot)
                                    print("Ingrese el id del parqueo: ")
                                    val parkingSpotStr = readLine()!!
                                    val currentParkingSpot = currentLevel.parkingLot.findParkingSpotById(parkingSpotStr)

                                    if(currentParkingSpot != null){
                                        //Hour to add the Car in the ParkingLot of the Level
                                        val currentLevelIndex = building.levels.indexOf(currentLevel)
                                        building.levels.get(currentLevelIndex).parkingLot.addCar(
                                                Car(plate, currentParkingSpot.row, currentParkingSpot.column))

                                    } else {
                                        println("Ocurrio un error, el id del parqueo seleccionado no es valido, " +
                                                "intente de nuevo..")
                                    }
                                } else {
                                    println("NO HAY ESPACIO DISPONIBLE EN NINGUN NIVEL")
                                }

                            } else { //There's a car with that plate at some level
                                val carLevel:Level = building.getLevelByCarPlate(plate)
                            }
                        }
                        2 -> {
                            wantContinuesDriver = false
                        }
                    }
                } while (wantContinuesDriver)
            }
            3 -> {
                wantContinues = false
            }
        }
    } while(wantContinues)
}

fun readFilePath(): String? {
    val dialog = FileDialog(Frame(), "Seleccione el archivo a abrir")
    dialog.mode = FileDialog.LOAD
    dialog.isVisible = true
    val file:String = ("$dialog.directory $dialog.file").replace("\\", "/")
    println("$file seleccionado.")
    return file
}

fun generateParkingLot(path: String?): ParkingLot? {
    if(!path.isNullOrEmpty()){
        val parkingLot = ParkingLot(0,0)

        val bufferedReader = File(path).bufferedReader()
        val atomicInteger:AtomicInteger = AtomicInteger()
        bufferedReader.useLines { lines -> lines.forEach {
            val row = atomicInteger.getAndIncrement()
            for(column in 0..it.length){
                val parkingItem = it.substring(column, column + 1)
                if(parkingItem != " "){
                    when(parkingItem){
                        WALL_CHARACTER -> {
                            parkingLot.addWall(Wall(row, column))
                        }
                        else -> { //Means that is a parking
                            if(!parkingLot.addParkingSpot(ParkingSpot(parkingItem, row, column))) return null
                        }
                    }
                }

            }

        }}

        return parkingLot
    }
    return null
}
