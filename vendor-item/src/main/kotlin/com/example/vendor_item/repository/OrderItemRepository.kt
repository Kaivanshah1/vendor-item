package com.example.vendor_item.repository

import com.example.vendor_item.model.OrderItem
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class OrderItemRepository(private val jdbcTemplate: JdbcTemplate) {

    private val rowMapper = RowMapper { rs, _ ->
        OrderItem(
            id = rs.getString("id"),
            orderId = rs.getString("order_id"),
            itemId = rs.getString("item_id"),
            quantity = rs.getInt("quantity"),
            createdAt = rs.getLong("created_at")
        )
    }

    fun findAll(): List<OrderItem> {
        val sql = "SELECT * FROM order_item"
        return jdbcTemplate.query(sql, rowMapper)
    }

    fun findById(id: String): OrderItem? {
        val sql = "SELECT * FROM order_item WHERE id = ?"
        return jdbcTemplate.query(sql, rowMapper, id).firstOrNull()
    }

    fun save(orderItem: OrderItem): Int {
        val sql = "INSERT INTO order_item (id, order_id, item_id, quantity, created_at) VALUES (?, ?, ?, ?, ?)"
        return jdbcTemplate.update(sql, orderItem.id, orderItem.orderId, orderItem.itemId, orderItem.quantity, orderItem.createdAt)
    }

    fun update(orderItem: OrderItem): Int {
        val sql = "UPDATE order_item SET order_id = ?, item_id = ?, quantity = ?, created_at = ? WHERE id = ?"
        return jdbcTemplate.update(sql, orderItem.orderId, orderItem.itemId, orderItem.quantity, orderItem.createdAt, orderItem.id)
    }

    fun deleteById(id: String): Int {
        val sql = "DELETE FROM order_item WHERE id = ?"
        return jdbcTemplate.update(sql, id)
    }
}