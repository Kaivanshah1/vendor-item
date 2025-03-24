package com.example.vendor_item.model

data class Item(
    val id: String?,
    val name: String,
    val price: Double,
    val vendorId: String?,
    val description: String?,
    val imageUrl: String?,
    val createdAt: Long?
)