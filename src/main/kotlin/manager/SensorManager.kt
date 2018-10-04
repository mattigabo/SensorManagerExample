package manager

import manager.spring.RestController
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableAutoConfiguration
@Configuration
class SensorManager{
    @Bean
    fun controller() = RestController()

    init{
        StationManager.init()
    }
}