package com.example.vendor_item.controller

import com.example.vendor_item.model.ListOrder
import com.example.vendor_item.model.Order
import com.example.vendor_item.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(private val orderService: OrderService) {

    @PostMapping("/list")
    fun getAllOrders(@RequestBody listOrder: ListOrder): ResponseEntity<List<Order>> {
        return ResponseEntity.ok(orderService.getAllOrders(listOrder))
    }

    @GetMapping("/get/{id}")
    fun getOrderById(@PathVariable id: String): ResponseEntity<Order> {
        val order = orderService.getOrderById(id)
        return if (order != null) ResponseEntity.ok(order) else ResponseEntity.notFound().build()
    }

    @PostMapping("/create")
    fun createOrder(@RequestBody order: Order): ResponseEntity<String> {
        return if (orderService.createOrder(order) > 0)
            ResponseEntity.ok("Order created successfully")
        else
            ResponseEntity.badRequest().body("Failed to create order")
    }

    @PostMapping("/update")
    fun updateOrder(@RequestBody order: Order): ResponseEntity<String> {
        return if (orderService.updateOrder(order) > 0)
            ResponseEntity.ok("Order updated successfully")
        else
            ResponseEntity.badRequest().body("Failed to update order")
    }

    @DeleteMapping("/delete/{id}")
    fun deleteOrder(@PathVariable id: String): ResponseEntity<String> {
        return if (orderService.deleteOrder(id) > 0)
            ResponseEntity.ok("Order deleted successfully")
        else
            ResponseEntity.notFound().build()
    }
}
