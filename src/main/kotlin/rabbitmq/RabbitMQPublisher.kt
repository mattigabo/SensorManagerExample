package rabbitmq

import Publisher

/**
 *
 * A Wrapper of a Publisher for RabbitMQ Broker
 * Created by Matteo Gabellini on 25/09/2018.
 */
class RabbitMQPublisher(val connector: BrokerConnector) : Publisher<String, String> {

    override fun publish(message: String, topic: String) {
        connector.channel.basicPublish(connector.exchangeName, topic, null, message.toByteArray(charset("UTF-8")))
        println(" [x] Sent '$message' on '${topic}' topic")
    }
}

fun main(argv: Array<String>) {
    BrokerConnector.init("localhost", "TestExchange")
    val pub = RabbitMQPublisher(BrokerConnector.INSTANCE)
    for (i in 0 until 10) {
        pub.publish(i.toString(), "hello")
    }

    BrokerConnector.INSTANCE.close()
}