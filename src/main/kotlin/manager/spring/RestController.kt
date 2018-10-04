package manager.spring

import manager.SensorValueRecord
import manager.StationManager
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import manager.SensorValuesStorage


@RestController
class RestController {

    @PostMapping("/addstation")
    fun addStation(@RequestBody newStation : Station): ResponseEntity<Unit> {
        StationManager.INSTANCE.addStation(newStation.name)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/stations")
    fun stations(): ResponseEntity<String> {
        val stations : MutableMap<String, MutableMap<String, SensorValuesStorage>> = StationManager.INSTANCE.stationsStorage

        return ResponseEntity.ok().body(GsonUtils.fromStructureToJson(stations))
    }

    @GetMapping("/stations/{station_name}")
    fun stationInfo(@PathVariable station_name: String): ResponseEntity<String> {
        val stationInfo : MutableMap<String, SensorValuesStorage>? = StationManager.INSTANCE.stationsStorage.get(station_name)
        return ResponseEntity.ok().body(GsonUtils.fromStructureToJson(stationInfo))
    }

    @GetMapping("/stations/{station_name}/{sensor_name}")
    fun sensorValue(@PathVariable station_name: String,
                    @PathVariable sensor_name: String): ResponseEntity<String> {
        val sensorValues : List<SensorValueRecord>? = StationManager.INSTANCE.getSensorValues(station_name, sensor_name)

        return ResponseEntity.ok().body(GsonUtils.fromStructureToJson(sensorValues))
    }
}