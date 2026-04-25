package edu.booking.hotel_booking.kafka.producer

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class BookingProducer (
    val kafkaTemplate: KafkaTemplate<String, String>
) {
    fun sendBookingInfo(message: String) =
        kafkaTemplate.send("booking", message)
}