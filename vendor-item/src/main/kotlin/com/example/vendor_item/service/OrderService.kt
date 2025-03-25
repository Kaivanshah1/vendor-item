package com.example.vendor_item.service

import com.example.vendor_item.model.ListOrder
import com.example.vendor_item.model.Order
import com.example.vendor_item.model.OrderItem
import com.example.vendor_item.repository.OrderItemRepository
import com.example.vendor_item.repository.OrderRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class OrderService(private val orderRepository: OrderRepository, private val orderItemRepository: OrderItemRepository) {

    fun getAllOrders(listOrder: ListOrder): List<Order> {
        return orderRepository.findAll(listOrder.search, listOrder.page, listOrder.size, listOrder.getAll, listOrder.userId)
    }

    fun getOrderById(id: String): Order? {
        return orderRepository.findById(id)
    }

    fun createOrder(order: Order): Int {
        val orderToBeCreated = Order(
            id = UUID.randomUUID().toString(),
            userId = order.userId,
            status = order.status ?: "pending", // Provide a default status
            createdAt = Instant.now().toEpochMilli()
        )

        // Save the order first
        val savedOrder = orderRepository.save(orderToBeCreated)

        // Safely handle order items
        val orderItems = order.items?.map { orderItem ->
            val orderedItem = OrderItem(
                id = UUID.randomUUID().toString(),
                orderId = orderToBeCreated.id,
                itemId = orderItem.itemId,
                quantity = orderItem.quantity,
                createdAt = Instant.now().toEpochMilli()
            )
            orderItemRepository.save(orderedItem)
        } ?: emptyList()

        return savedOrder;
    }

    fun updateOrder(order: Order): Int {
        val orderToBeUpdated = Order(
            id = order.id,
            userId = order.userId,
            status = order.status ?: "done",
            items = order.items,
            createdAt = order.createdAt
        )

        // Save the updated order
        val updatedOrder = orderRepository.update(orderToBeUpdated)

        // Handle order items update
        val updatedOrderItems = order.items?.map { orderItem ->
            val updatedOrderItem = OrderItem(
                id = orderItem.id,
                orderId = orderItem.id,
                itemId = orderItem.itemId,
                quantity = orderItem.quantity,
                createdAt = orderItem.createdAt
            )
            orderItemRepository.update(updatedOrderItem)
        } ?: emptyList()

        return updatedOrder
    }

    fun deleteOrder(id: String): Int {
        return orderRepository.deleteById(id)
    }
}
