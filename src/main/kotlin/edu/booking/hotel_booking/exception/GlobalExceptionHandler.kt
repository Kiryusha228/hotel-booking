package edu.booking.hotel_booking.exception

import edu.booking.hotel_booking.dto.ExceptionDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime


@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException::class)
    fun handleException(ex: RuntimeException, request: WebRequest): ResponseEntity<ExceptionDto> {

        val details = mutableMapOf<String, Any>(
            "path" to request.getDescription(false),
            "timestamp" to LocalDateTime.now()
        )

        val errorResponse = ExceptionDto(ex.message ?: "Unknown error", details)
        ex.printStackTrace()

        return ResponseEntity(errorResponse, getStatus(ex.javaClass))
    }

    private fun getStatus(exceptionClass: Class<out Throwable>): HttpStatus {
        val responseStatus = exceptionClass.getAnnotation(ResponseStatus::class.java)
        return responseStatus?.value ?: HttpStatus.INTERNAL_SERVER_ERROR
    }
}