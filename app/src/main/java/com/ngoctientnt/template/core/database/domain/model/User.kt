package com.ngoctientnt.template.core.database.domain.model

data class User(
    val id: Int = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
)
