package manager

import com.rabbitmq.client.Consumer
import rabbitmq.BrokerConnector
import rabbitmq.RabbitMQSubscriber
import station.SensorsStationService
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by Matteo Gabellini on 02/10/2018.
 */
class StationManager private constructor() {

    val subscriber: RabbitMQSubscriber
    val stationsStorage: MutableMap<String, MutableMap<String, SensorValuesStorage>>

    var storageLimit = 10

    /*companion object {
        lateinit var INSTANCE: StationManager
        val isInitialized = AtomicBoolean()
        fun init(){
            if(!isInitialized.getAndSet(true)){
                INSTANCE = StationManager()
            }
        }
    }*/

    private object GetInstance {
        val INSTANCE = StationManager()
    }

    companion object {
        val INSTANCE: StationManager by lazy { GetInstance.INSTANCE }
    }


    init{
        BrokerConnector.init("localhost", SensorsStationService.EXCHANGE_NAME)
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
