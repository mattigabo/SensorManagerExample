package spring

import org.springframework.boot.SpringApplication
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

/**
 * Run the application
 * @param args The command line arguments
 */
fun main(args: Array<String>) {
    SpringApplication.run(RestAdapter::class.java, *args)
}