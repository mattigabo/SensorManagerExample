package serviceboot

import manager.StationManager
import com.github.kittinunf.fuel.Fuel

/**
 * This is a basic function that simulate a external user application that interact with the SensorManager REST API
 */
fun main(args: Array<String>) {
    //Before start this process boot all the other services

    val SENSOR_MANAGER_ROUTE = "http://localhost:8080/"


    println("Adding the station: Station1...");

    var request = Fuel.post(SENSOR_MANAGER_ROUTE + "addstation").body("{ \"name\" : \"Station1\" }")
    request.headers["Content-Type"] = "application/json";
    request.response();

    Thread.sleep(6000)


    println("Requesting all station data...")

    request = Fuel.get(SENSOR_MANAGER_ROUTE + "stations")
    request.headers["Content-Type"] = "application/json";
    var response = request.response();

    println("RESPONSE...")
    println(response.second);



    println("Requesting oxygen sensor data of Station1...")

    request = Fuel.get(SENSOR_MANAGER_ROUTE + "stations/Station1/" + SensorTypes.OXYGEN)
    request.headers["Content-Type"] = "application/json";
    response = request.response();

    println("RESPONSE...")
    println(response.second);



    println("Adding the station: Station2...");

    request = Fuel.post(SENSOR_MANAGER_ROUTE + "addstation").body("{ \"name\" : \"Station2\" }")
    request.headers["Content-Type"] = "application/json";
    request.response();



    Thread.sleep(6000)



    println("Requesting humidity sensor data of Station1...")

    request = Fuel.get(SENSOR_MANAGER_ROUTE + "stations/Station1/" + SensorTypes.HUMIDITY)
    request.headers["Content-Type"] = "application/json";
    response = request.response();

    println("RESPONSE...")
    println(response.second);



    println("Requesting oxygen sensor data of Station2...")

    request = Fuel.get(SENSOR_MANAGER_ROUTE + "stations/Station2/" + SensorTypes.OXYGEN)
    request.headers["Content-Type"] = "application/json";
    response = request.response();

    println("RESPONSE...")
    println(response.second);


}