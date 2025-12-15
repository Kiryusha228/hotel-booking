package edu.booking.hotel_booking

import edu.booking.hotel_booking.dao.RoomDao
import edu.booking.hotel_booking.dto.Room
import edu.booking.hotel_booking.entity.RoomEntity
import edu.booking.hotel_booking.exception.RoomNotFoundException
import edu.booking.hotel_booking.service.IdGenerator
import edu.booking.hotel_booking.service.RoomService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals

class RoomServiceTest {
    private lateinit var roomService: RoomService

    private val roomDao = mockk<RoomDao>()
    private val generator = mockk<IdGenerator>()

    @BeforeEach
    fun setUp() {
        roomService = RoomService(roomDao, generator)
    }

    @Test
    fun createRoomTest() {
        //arrange
        val expectedId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val request = Room(2, 201, 3)
        val expectedEntity = RoomEntity(expectedId, request.floor, request.number, request.beds)

        every { generator.generate() } returns expectedId
        every { roomDao.createRoom(expectedEntity) } returns expectedEntity

        //act
        val result = roomService.addRoom(request)

        //assert
        assertEquals(result, expectedEntity)
    }

    @Test
    fun updateRoomTest() {
        //arrange
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val request = Room(2, 201, 3)
        val expectedEntity = RoomEntity(id, request.floor, request.number, request.beds)

        every { roomDao.updateRoom(expectedEntity) } returns expectedEntity
        every { roomDao.existsById(id) } returns true

        //act
        val result = roomService.updateRoom(id, request)

        //assert
        assertEquals(result, expectedEntity)
    }

    @Test
    fun updateRoomExceptionTest() {
        //arrange
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val request = Room(2, 201, 3)
        val expectedEntity = RoomEntity(id, request.floor, request.number, request.beds)

        every { roomDao.updateRoom(expectedEntity) } returns expectedEntity
        every { roomDao.existsById(id) } returns false

        //act
        val exception = assertThrows<RoomNotFoundException> {
            roomService.updateRoom(id, request)
        }

        //assert
        assertEquals("Комната не найдена!", exception.message)

    }

    @Test
    fun deleteRoomTest() {
        //arrange
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")

        every { roomDao.deleteRoom(id) } returns id
        every { roomDao.existsById(id) } returns true

        //act
        val result = roomService.deleteRoom(id)

        //assert
        assertEquals(result, id)
    }

    @Test
    fun deleteRoomExceptionTest() {
        //arrange
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")

        every { roomDao.deleteRoom(id) } returns id
        every { roomDao.existsById(id) } returns false

        //act
        val exception = assertThrows<RoomNotFoundException> {
            roomService.deleteRoom(id)
        }

        //assert
        assertEquals("Комната не найдена!", exception.message)
    }







}