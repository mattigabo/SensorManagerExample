package manager

import com.rabbitmq.client.Consumer
import rabbitmq.BrokerConnector
import rabbitmq.RabbitMQSubscriber
import station.SensorsStation
import java.time.LocalDateTime
import kotlin.collections.HashMap


class SensorManager{

    val subscriber: RabbitMQSubscriber
    val stationsStorage: MutableMap<String, MutableMap<String, SensorValuesStorage>>

    var storageLimit = 10

    init{
        BrokerConnector.init("localhost", SensorsStation.EXCHANGE_NAME)
        subscriber = RabbitMQSubscriber(BrokerConnector.INSTANCE)
        stationsStorage = HashMap()
    }

    fun addStation(name: String){
        addStationToStorage(name)

        val oxygenValuesConsumer = generateSensorsStorageInjector(name, SensorTypes.OXYGEN)
        subscriber.subscribe(SensorTypes.OXYGEN + "." + name, oxygenValuesConsumer)

        val humidityValuesConsumer = generateSensorsStorageInjector(name, SensorTypes.HUMIDITY)
        subscriber.subscribe(SensorTypes.HUMIDITY + "." + name, humidityValuesConsumer)
    }

    fun getSensorValues(stationName: String, sensorType: String): List<SensorValueRecord>? {
        return stationsStorage.get(stationName)?.get(sensorType)?.peekAll()
    }

    private fun addStationToStorage(name: String){
        val sensorsStorage = HashMap<String, SensorValuesStorage>()
        sensorsStorage.put(SensorTypes.OXYGEN, SensorValuesStorage(storageLimit))
        sensorsStorage.put(SensorTypes.HUMIDITY, SensorValuesStorage(storageLimit))

        stationsStorage.put(name, sensorsStorage)
    }

    private fun generateSensorsStorageInjector(stationName: String, sensorType: String) : Consumer {
        return subscriber.createStringConsumer { sensorValue ->
            val record = SensorValueRecord(Integer.parseInt(sensorValue), LocalDateTime.now())

            stationsStorage.get(stationName)!!.get(sensorType)!!.store(record)
        }
    }
}


fun main(args: Array<String>) {
    val sm = SensorManager()
    sm.addStation("Station1")
    Thread.sleep(6000)

    sm.getSensorValues("Station1", SensorTypes.OXYGEN)?.forEach {
        println("Station 1 Sensor: Oxygen value:" + it.value + " recorded: " + it.time);
    }


    sm.addStation("Station2")
    Thread.sleep(6000)


    sm.getSensorValues("Station1", SensorTypes.OXYGEN)?.forEach {
        println("Station 1 Sensor: Oxygen value:" + it.value + " recorded: " + it.time);
    }

    sm.getSensorValues("Station2", SensorTypes.OXYGEN)?.forEach {
        println("Station 2 Sensor: Oxygen value:" + it.value + " recorded: " + it.time);
    }

}