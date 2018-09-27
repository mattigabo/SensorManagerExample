package rabbitmq

import com.rabbitmq.client.*
import Subscriber

/**
 *
 * Wrapper of a Subscriber for RabbitMq Broker
 *
 * Created by Matteo Gabellini on 25/09/2018.
 */
class RabbitMQSubscriber(val connector: BrokerConnector) : Subscriber<String, Consumer> {

    private var subscribedConsumer: HashMap<String, String> = HashMap()
    private var bindedQueue: HashMap<String, String> = HashMap()

    override fun subscribe(topic: String, consumingLogic: Consumer) {
        if (!subscribedConsumer.containsKey(topic)) {
            val queueName: String = connector.getNewQueue()

            connector.channel.queueBind(queueName, connector.exchangeName, topic)
            bindedQueue.put(topic, queueName)
            subscribedConsumer.put(
                    topic,
                    connector.channel.basicConsume(queueName, true, consumingLogic)
            )
        }
    }

    override fun unsubscribe(topic: String) {
        connector.channel.basicCancel(subscribedConsumer.get(topic))
        connector.channel.queueUnbind(bindedQueue.get(topic), connector.exchangeName, topic)
        subscribedConsumer.remove(topic)
    }


    override fun subscribedTopics(): Set<String> {
        val set = subscribedConsumer.keys
        return set
    }

    fun createStringConsumer(messageHandler: (String) -> Any): Consumer {
        return object : DefaultConsumer(connector.channel) {
            @Throws(java.io.IOException::class)
            override fun handleDelivery(consumerTag: String,
                                        envelope: Envelope,
                                        properties: AMQP.BasicProperties,
                                        body: ByteArray) {
                val message = String(body, Charsets.UTF_8)
                messageHandler(message)
            }
        }
    }
}

fun main(argv: Array<String>) {
    BrokerConnector.init("localhost", "TestExchange")
    val sub = RabbitMQSubscriber(BrokerConnector.INSTANCE)

    val consumer = sub.createStringConsumer { X ->
        println(X)
    }
    sub.subscribe("hello", consumer)
}