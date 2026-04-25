package edu.booking.hotel_booking.kafka.producer

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class RoomProducer (
    val kafkaTemplate: KafkaTemplate<String, String>
) {
    fun sendRoomInfo(message: String) =
        kafkaTemplate.send("room", message)
}