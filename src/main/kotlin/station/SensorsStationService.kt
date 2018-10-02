package station

import rabbitmq.BrokerConnector
import rabbitmq.RabbitMQPublisher

/**
 * Created by Matteo Gabellini on 27/09/2018.
 */
class SensorsStationService(val name: String) {

    private val observationPeriod = 5000L
    private val sensorSimulationPeriod = 2000L

    private val oxygenSensor: ObservableSensor
    private val oxygenTopicName: String

    private val humiditySensor: ObservableSensor
    private val humidityTopicName: String

    val publisher: RabbitMQPublisher

    init{
        BrokerConnector.init("localhost", SensorsStationService.EXCHANGE_NAME)
        publisher = RabbitMQPublisher(BrokerConnector.INSTANCE)

        oxygenSensor = ObservableSensor(SimulatedSensor(SensorTypes.OXYGEN, sensorSimulationPeriod))
        oxygenTopicName = oxygenSensor.name + "." + name
        oxygenSensor.createObservable(observationPeriod).subscribe{
            this.publisher.publish(it.toString(), oxygenTopicName)
        }


        humiditySensor = ObservableSensor(SimulatedSensor(SensorTypes.HUMIDITY, sensorSimulationPeriod))
        humidityTopicName = humiditySensor.name + "." + name
        humiditySensor.createObservable(observationPeriod).subscribe{
            this.publisher.publish(it.toString(), humidityTopicName)
        }
    }

    companion object {
        val EXCHANGE_NAME = "StationExchange"
    }
}


fun main(argv: Array<String>) {
    val station1 = SensorsStationService("Station1");
    //val station2 = SensorsStationService("Station2");
}
