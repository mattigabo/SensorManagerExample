package serviceboot

import manager.SensorManager
import org.springframework.boot.SpringApplication

fun main(args: Array<String>) {
    SpringApplication.run(SensorManager::class.java, *args)
}