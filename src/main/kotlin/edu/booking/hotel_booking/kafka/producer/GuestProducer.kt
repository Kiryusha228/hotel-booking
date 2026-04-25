package edu.booking.hotel_booking.kafka.producer

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class GuestProducer (
    val kafkaTemplate: KafkaTemplate<String, String>
) {
    fun sendGuestInfo(message: String) =
        kafkaTemplate.send("guest", message)
}