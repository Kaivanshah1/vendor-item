package com.example.vendor_item.model

data class Order(
    val id: String?,
    val userId: String?,
    val status: String?,
    val items: MutableList<OrderItem> = mutableListOf(),
    val createdAt: Long?
)

data class OrderItem(
    val id: String?,
    val orderId: String?,
    val itemId: String?,
    val quantity: Int,
    val createdAt: Long?
)

data class ListOrder(
    val search: String?,
    val page: Int?,
    val size: Int?,
    val getAll: Boolean?,
    val userId: String
)