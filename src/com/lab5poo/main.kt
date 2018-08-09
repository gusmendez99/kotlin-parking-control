package com.lab5poo

import com.lab5poo.car.Car
import com.lab5poo.level.Level
import com.lab5poo.parking.ParkingBuilding
import com.lab5poo.parking.ParkingSpot
import com.lab5poo.wall.WALL_CHARACTER
import com.lab5poo.wall.Wall
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.exitProcess

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

fun main(args: Array<String>) { //TODO: Fix logic about Car in Parking Spots
    val building:ParkingBuilding = ParkingBuilding()
    var wantContinues:Boolean = true
    var wantContinuesAdmin:Boolean
    var wantContinuesDriver:Boolean

    do {
        println(getMenu(MENU_MAIN))
        print("Ingrese una opcion: ")
        val strOption = readLine()!!
        val option = strOption.toInt()
        when(option){
            MENU_ADMIN -> { //MENU: ADMIN
                do {
                    wantContinuesAdmin = true
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
                            print("Seleccione el archivo de estructura(Busque el cuadro de dialogo que se abrio): ")
                            val path:String? = readFilePath()
                            val parkingLevel:Level? = generateParkingLevel(name, id, color, path)
                            if(building.addLevel(parkingLevel!!)){
                                println("NIVEL $id AGREGADO EXISTOSAMENTE\n")
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
                            if(building.getLevels().isNotEmpty()){
                                println(building)
                            } else {
                                println("No hay niveles por ahora, agrega uno...")
                            }
                        }
                        4 -> {
                            wantContinuesAdmin = false
                        }
                    }
                } while (wantContinuesAdmin)
            }
            MENU_DRIVER -> { //MENU: DRIVER
                do {
                    wantContinuesDriver = true
                    println(getMenu(MENU_DRIVER))
                    print("Ingrese una opcion: ")
                    val strOption = readLine()!!
                    val option = strOption.toInt()
                    when(option){
                        1 -> { //Plate Input
                            print("Ingrese la placa del carro: ")
                            val plate = readLine()!!
                            if(!building.findCarByPlate(plate)){ //Car Register
                                if(building.getLevelsWithAvailableSpaces().isNotEmpty()){
                                    println(building.getAvailableLevels())
                                    print("Ingrese un nivel : ")
                                    val levelStr = readLine()!!
                                    val levelSelected = levelStr.toInt()
                                    val currentLevel = building.getLevelsWithAvailableSpaces()[levelSelected - 1]
                                    println(currentLevel)
                                    print("Ingrese el id del parqueo: ")
                                    val parkingSpotStr = readLine()!!
                                    val currentParkingSpot = currentLevel.findParkingSpotById(parkingSpotStr)
                                    if(currentParkingSpot != null){ //Hour to add the Car in the ParkingLot of the Level
                                        val currentLevelIndex = building.getLevels().indexOf(currentLevel)
                                        building.getLevels().get(currentLevelIndex).addCar(
                                                Car(plate, currentParkingSpot.getRow(), currentParkingSpot.getColumn()))
                                        println("CARRO AGREGADO EN EL ESTACIONAMIENTO $parkingSpotStr")
                                    } else {
                                        println("Ocurrio un error, el id del parqueo seleccionado no es valido, " +
                                                "intente de nuevo..")
                                    }
                                } else {
                                    println("NO HAY ESPACIO DISPONIBLE EN NINGUN NIVEL")
                                }
                            } else { //There's a car with that plate at some level
                                val carLevel:Level? = building.getLevelByCarPlate(plate)
                                if(carLevel != null){
                                    val levelInfo = "NOMBRE: ${carLevel.getName()}, ID: ${carLevel.getId()}, " +
                                            "COLOR: ${carLevel.getColor()} "
                                    println("La placa $plate se encuentra parqueada en el parqueo: $levelInfo")
                                    val car = carLevel.findCarByPlate(plate)
                                    if(car != null){
                                        val parkingSpotCar = carLevel.getParkingSpotAt(car.getRow(), car.getColumn())!!
                                        println("El carro se encuentra parqueado en el parqueo con ID: ${parkingSpotCar.getId()}")
                                        println(carLevel)
                                    }
                                } else {
                                    println("NO SE ENCONTRARON COINCIDENCIAS")
                                }
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

    exitProcess(1)
}

fun readFilePath(): String? {
    val dialog = FileDialog(Frame(), "Seleccione el archivo a abrir")
    dialog.mode = FileDialog.LOAD
    dialog.isVisible = true
    val file:String = ("${dialog.directory}${dialog.file}").replace("\\", "/")
    println("$file seleccionado.")
    return file
}

fun generateParkingLevel(name:String, id:String, color:String, path: String?): Level? {
    if(!path.isNullOrEmpty()){
        val newLevel:Level = Level(name, id, color)

        var bufferedReader = File(path).bufferedReader()
        val atomicInteger:AtomicInteger = AtomicInteger()
        bufferedReader.useLines { lines -> lines.forEach {
            println(it)
            val row = atomicInteger.getAndIncrement()
            for(column in 0 until it.length){
                val parkingItem = it.substring(column, column + 1)
                if(parkingItem != " "){
                    when(parkingItem){
                        WALL_CHARACTER -> {
                            newLevel.addWall(Wall(row, column))
                        }
                        else -> { //Means that is a parking
                            if(!newLevel.addParkingSpot(ParkingSpot(parkingItem, row, column))) return null
                        }
                    }
                }
            }
        }}
        bufferedReader = File(path).bufferedReader()
        newLevel.setWidth(bufferedReader.readLines().first()!!.length)
        newLevel.setHeight(atomicInteger.get())
        return newLevel
    }
    return null
}
