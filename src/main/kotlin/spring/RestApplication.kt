package spring

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * The main entry point to the Pinger application.
 */
@EnableAutoConfiguration
@Configuration
internal class RestAdapter {
    @Bean
    fun controller() = RestController()
}