package manager

import com.rabbitmq.client.Consumer
import rabbitmq.BrokerConnector
import rabbitmq.RabbitMQSubscriber
import station.SensorStation
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicBoolean

class StationManager private constructor() {

    val subscriber: RabbitMQSubscriber
    val stationsStorage: MutableMap<String, MutableMap<String, SensorValuesStorage>>

    var storageLimit = 10

    companion object {
        lateinit var INSTANCE: StationManager
        val isInitialized = AtomicBoolean()
        fun init(){
            if(!isInitialized.getAndSet(true)){
                INSTANCE = StationManager()
            }
        }
    }


    init{
        BrokerConnector.init("localhost", SensorStation.EXCHANGE_NAME)
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
