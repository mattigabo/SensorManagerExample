package manager

import org.springframework.boot.SpringApplication
import spring.RestAdapter


class SensorManager{

    var stationManager = StationManager.INSTANCE
    //var restController = RestController(stationManager)
}


fun main(args: Array<String>) {
    SpringApplication.run(RestAdapter::class.java, *args)
}