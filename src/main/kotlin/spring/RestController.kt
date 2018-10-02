package spring

import com.github.kittinunf.fuel.Fuel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

/**
 * The controller of the Pinger entity.
 */
@RestController
class RestController {

    @PostMapping("/addstation")
    fun addStation(): String {

        return "Pong received..."
    }

    @GetMapping("/stations")
    fun stations(): String {
        return "Pong received..."
    }

    @GetMapping("/stations/asd")
    fun stationInfo(): String {
        return "Pong received..."
    }

    @GetMapping("/stations/asd")
    fun sensorValue(): String {
        return "Pong received..."
    }
}