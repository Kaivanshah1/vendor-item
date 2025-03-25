package com.example.vendor_item.repository

import com.example.vendor_item.model.Item
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class ItemRepository(private val jdbcTemplate: JdbcTemplate) {

    private val rowMapper = RowMapper { rs, _ ->
        Item(
            id = rs.getString("id"),
            name = rs.getString("name"),
            price = rs.getDouble("price"),
            vendorId = rs.getString("vendor_id"),
            description = rs.getString("description"),
            imageUrl = rs.getString("image_url"),
            createdAt = rs.getLong("created_at")
        )
    }

    fun findAll(search: String?, page: Int?, size: Int?, getAll: Boolean?): List<Item> {
        var sql = "SELECT * FROM item"
        val params = mutableListOf<Any>()
        val whereClauses = mutableListOf<String>()

        if (!search.isNullOrEmpty()) {
            whereClauses.add("name ILIKE ?")
            params.add("%$search%")
        }

        // add where conditions if any
        if (whereClauses.isNotEmpty()) {
            sql += " WHERE " + whereClauses.joinToString(" AND ")
        }

        if (getAll != true) { // Only add limit and offset if getAll is NOT true.
            val limit = if (size != null && size > 0) size else 10
            val offset = if (page != null && page > 0) (page - 1) * limit else 0
            sql += " LIMIT ? OFFSET ?"
            params.add(limit)
            params.add(offset)
        }

        return jdbcTemplate.query(sql, params.toTypedArray(), rowMapper)
    }

    fun findById(id: String): Item? {
        val sql = "SELECT * FROM item WHERE id = ?"
        return jdbcTemplate.query(sql, rowMapper, id).firstOrNull()
    }

    fun save(item: Item): Int {
        val sql = "INSERT INTO item (id, name, price, vendor_id, description, image_url, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)"
        return jdbcTemplate.update(sql, item.id, item.name, item.price, item.vendorId, item.description, item.imageUrl, item.createdAt)
    }

    fun update(item: Item): Int {
        val sql = "UPDATE item SET name = ?, price = ?, vendor_id = ?, description = ?, image_url = ?, created_at = ? WHERE id = ?"
        return jdbcTemplate.update(sql, item.name, item.price, item.vendorId, item.description, item.imageUrl, item.createdAt, item.id)
    }

    fun deleteById(id: String): Int {
        val sql = "DELETE FROM item WHERE id = ?"
        return jdbcTemplate.update(sql, id)
    }
}