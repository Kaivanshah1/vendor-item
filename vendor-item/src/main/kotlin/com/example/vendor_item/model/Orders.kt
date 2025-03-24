package com.example.vendor_item.model

data class Order(
    val id: String?,
    val userId: String?,
    val status: String?,
    val createdAt: Long?
)

data class OrderItem(
    val id: String?,
    val orderId: String?,
    val itemId: String?,
    val quantity: Int,
    val createdAt: Long?
)


