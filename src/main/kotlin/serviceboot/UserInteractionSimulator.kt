package serviceboot

import manager.StationManager
import com.github.kittinunf.fuel.Fuel

/**
 * This is a basic function that simulate a external user application that interact with the SensorManager REST API
 */
fun main(args: Array<String>) {
    //Before start this process boot all the other services

    val SENSOR_MANAGER_ROUTE = "http://localhost:8080/"

    val sm = StationManager.INSTANCE
    var request = Fuel.post(SENSOR_MANAGER_ROUTE + "addstation").body("{ \"name\" : \"Station1\" }")
    request.headers["Content-Type"] = "application/json";
    request.response();

    Thread.sleep(6000)

    sm.getSensorValues("Station1", SensorTypes.OXYGEN)?.forEach {
        println("Station 1 Sensor: Oxygen value:" + it.value + " recorded: " + it.time);
    }

    request = Fuel.post(SENSOR_MANAGER_ROUTE + "addstation").body("{ \"name\" : \"Station2\" }")
    request.headers["Content-Type"] = "application/json";
    request.response();
    Thread.sleep(6000)


    sm.getSensorValues("Station1", SensorTypes.OXYGEN)?.forEach {
        println("Station 1 Sensor: Oxygen value:" + it.value + " recorded: " + it.time);
    }

    sm.getSensorValues("Station2", SensorTypes.OXYGEN)?.forEach {
        println("Station 2 Sensor: Oxygen value:" + it.value + " recorded: " + it.time);
    }
}