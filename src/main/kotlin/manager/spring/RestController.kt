package spring

import manager.SensorValueRecord
import manager.StationManager
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import manager.SensorValuesStorage


/**
 * The controller of the Pinger entity.
 */
@RestController
class RestController {

    @PostMapping("/addstation")
    fun addStation(@RequestBody newStation : Station): ResponseEntity<Unit> {
        StationManager.INSTANCE.addStation(newStation.name)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/stations")
    fun stations(): ResponseEntity<String> {
        //val stations : MutableMap<String, MutableMap<String, SensorValuesStorage>> = StationManager.INSTANCE.stationsStorage
        val simulatedStations : MutableMap<String, MutableMap<String, SensorValuesStorage>> =
                mutableMapOf(Pair("station1", mutableMapOf(Pair("asd", SensorValuesStorage(1)),
                        Pair("asd2", SensorValuesStorage(1)),
                        Pair("asd3", SensorValuesStorage(1)))),
                        Pair("station2", mutableMapOf(Pair("asd", SensorValuesStorage(1)),
                                Pair("asd2", SensorValuesStorage(1)),
                                Pair("asd3", SensorValuesStorage(1)))))

        return ResponseEntity.ok().body(GsonUtils.fromStructureToJson(simulatedStations))
    }

    @GetMapping("/stations/{station_name}")
    fun stationInfo(@PathVariable station_name: String): ResponseEntity<String> {
        //val stationInfo : MutableMap<String, SensorValuesStorage>? = StationManager.INSTANCE.stationsStorage.get(station_name)
        val simulatedInfo = mutableMapOf(Pair("asd", SensorValuesStorage(1)),
                Pair("asd2", SensorValuesStorage(1)),
                Pair("asd3", SensorValuesStorage(1)))

        return ResponseEntity.ok().body(GsonUtils.fromStructureToJson(simulatedInfo))
    }

    @GetMapping("/stations/{station_name}/{sensor_name}")
    fun sensorValue(@PathVariable station_name: String,
                    @PathVariable sensor_name: String): ResponseEntity<String> {
        //val sensorValues : List<SensorValueRecord>? = StationManager.INSTANCE.getSensorValues(station_name, sensor_name)
        val simulatedValues : List<SensorValueRecord>? = listOf(SensorValueRecord(1, LocalDateTime.now()),
                SensorValueRecord(1, LocalDateTime.now()),
                SensorValueRecord(1, LocalDateTime.now()))

        return ResponseEntity.ok().body(GsonUtils.fromStructureToJson(simulatedValues))
    }
}