package com.example.vendor_item.repository

import com.example.vendor_item.model.Order
import com.example.vendor_item.model.OrderItem
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class OrderRepository(private val jdbcTemplate: JdbcTemplate) {

    private val rowMapper = RowMapper { rs, _ ->
        Order(
            id = rs.getString("id"),
            userId = rs.getString("user_id"),
            status = rs.getString("status"),
            createdAt = rs.getLong("created_at")
        )
    }

    fun findAll(search: String?, page: Int?, size: Int?, getAll: Boolean?, userId: String): List<Order> {
        var sql = """
            SELECT o.id, o.user_id, o.status, o.created_at,
                   oi.id AS order_item_id, oi.item_id, oi.quantity, oi.created_at AS order_item_created_at
            FROM orders o
            LEFT JOIN order_item oi ON o.id = oi.order_id
            WHERE o.user_id = ?
        """.trimIndent()
        val params = mutableListOf<Any>(userId)
        val whereClauses = mutableListOf<String>()

        if (!search.isNullOrEmpty()) {
            whereClauses.add("o.status ILIKE ?")
            params.add("%$search%")
        }

        if (whereClauses.isNotEmpty()) {
            sql += " AND " + whereClauses.joinToString(" AND ")
        }

        if (getAll != true) {
            val limit = if (size != null && size > 0) size else 10
            val offset = if (page != null && page > 0) (page - 1) * limit else 0
            sql += " LIMIT ? OFFSET ?"
            params.add(limit)
            params.add(offset)
        }

        val orders = mutableListOf<Order>()
        val orderMap = mutableMapOf<String, Order>()

        jdbcTemplate.query(sql, params.toTypedArray()) { rs ->
            val orderId = rs.getString("id")
            val order = orderMap.getOrPut(orderId) {
                Order(
                    id = orderId,
                    userId = rs.getString("user_id"),
                    status = rs.getString("status"),
                    createdAt = rs.getLong("created_at")
                ).also { orders.add(it) }
            }

            val orderItemId = rs.getString("order_item_id")
            if (orderItemId != null) {
                order.items.add(
                    OrderItem(
                        id = orderItemId,
                        orderId = orderId,
                        itemId = rs.getString("item_id"),
                        quantity = rs.getInt("quantity"),
                        createdAt = rs.getLong("order_item_created_at")
                    )
                )
            }
        }
        return orders
    }

    fun findById(id: String): Order? {
        val sql = "SELECT * FROM orders WHERE id = ?"
        return jdbcTemplate.query(sql, rowMapper, id).firstOrNull()
    }

    fun save(order: Order): Int {
        val sql = "INSERT INTO orders (id, user_id, status, created_at) VALUES (?, ?, ?, ?)"
        return jdbcTemplate.update(sql, order.id, order.userId, order.status, order.createdAt)
    }

    fun update(order: Order): Int {
        val sql = "UPDATE orders SET user_id = ?, status = ?, created_at = ? WHERE id = ?"
        return jdbcTemplate.update(sql, order.userId, order.status, order.createdAt, order.id)
    }

    fun deleteById(id: String): Int {
        val sql = "DELETE FROM orders WHERE id = ?"
        return jdbcTemplate.update(sql, id)
    }
}
