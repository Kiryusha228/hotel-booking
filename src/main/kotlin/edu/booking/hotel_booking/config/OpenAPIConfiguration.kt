package edu.booking.hotel_booking.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAPIConfiguration {
    @Bean
    fun defineOpenAPI(): OpenAPI? {
        val server = Server()
        val serverUrl = "http://localhost:8080" //заменить на .env в реальном проекте
        server.url = serverUrl
        server.description = "Development"

        val myContact = Contact()
        myContact.name = "Аббасов К.Н."
        myContact.email = "email@example.com"

        val info = Info()
            .title("Системное API для бронирования номеров в отеле")
            .version("1.0")
            .description("Это API предоставляет эндпоинты для управления бронированием отеля, позволяет " +
                    "работать с комнатами, посетителями и бронью.")
            .contact(myContact)
        return OpenAPI().info(info).servers(listOf(server))
    }
}