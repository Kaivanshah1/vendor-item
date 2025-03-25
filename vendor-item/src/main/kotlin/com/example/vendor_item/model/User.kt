package com.example.vendor_item.model

data class User(
    val id: String?,
    val name: String,
    val email: String?,
    val phone: String?,
    val createdAt: Long?
)

data class ListUser(
    val search: String?,
    val page: Int?,
    val size: Int?,
    val getAll: Boolean?
)