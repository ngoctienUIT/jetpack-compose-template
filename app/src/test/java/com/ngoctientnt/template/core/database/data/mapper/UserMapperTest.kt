package com.ngoctientnt.template.core.database.data.mapper

import com.ngoctientnt.template.core.database.domain.model.User
import com.ngoctientnt.template.data.local.entity.UserEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class UserMapperTest {

    @Test
    fun toDomain_mapsAllFields() {
        val entity = UserEntity(id = 1, name = "Alice", createdAt = 1000L)

        val domain = entity.toDomain()

        assertEquals(1, domain.id)
        assertEquals("Alice", domain.name)
        assertEquals(1000L, domain.createdAt)
    }

    @Test
    fun toEntity_mapsAllFields() {
        val user = User(id = 2, name = "Bob", createdAt = 2000L)

        val entity = user.toEntity()

        assertEquals(2, entity.id)
        assertEquals("Bob", entity.name)
        assertEquals(2000L, entity.createdAt)
    }

    @Test
    fun roundTrip_preservesData() {
        val original = User(id = 3, name = "Carol", createdAt = 3000L)

        val roundTripped = original.toEntity().toDomain()

        assertEquals(original, roundTripped)
    }
}
