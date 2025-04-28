package org.example.travelingapp.data.mapper

import org.example.travelingapp.core.response.user.UserListResponse
import org.example.travelingapp.core.response.user.dtos.UserDto
import org.example.travelingapp.data.local.entities.UserEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class UserMapperTest {

    @Test
    fun toUser_mapsEntityIntoDomainModel() {
        val entity = UserEntity(
            id = "1",
            username = "john",
            email = "john@example.com",
            updateAt = 100L
        )

        val user = entity.toUser()

        assertEquals("john", user.username)
    }

    @Test
    fun toEntities_mapsNullFieldsUsingFallbacks() {
        val response = UserListResponse().apply {
            data = listOf(
                UserDto(
                    id = null,
                    username = null,
                    email = null,
                    updatedAt = 42L
                )
            )
        }

        val entities = response.toEntities()

        assertEquals(1, entities.size)
        assertEquals("", entities[0].id)
        assertEquals("", entities[0].username)
        assertEquals("", entities[0].email)
        assertEquals(42L, entities[0].updateAt)
    }
}
