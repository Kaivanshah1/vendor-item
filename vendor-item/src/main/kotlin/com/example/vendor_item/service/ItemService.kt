package com.example.vendor_item.service

import com.example.vendor_item.model.Item
import com.example.vendor_item.model.ListItem
import com.example.vendor_item.repository.ItemRepository
import org.springframework.stereotype.Service

@Service
class ItemService(private val itemRepository: ItemRepository) {

    fun getAllItems(listItem: ListItem): List<Item> {
        return itemRepository.findAll(listItem.search, listItem.page, listItem.size, listItem.getAll)
    }

    fun getItemById(id: String): Item? {
        return itemRepository.findById(id)
    }

    fun createItem(item: Item): Int {
        return itemRepository.save(item)
    }

    fun updateItem(item: Item): Int {
        return itemRepository.update(item)
    }

    fun deleteItem(id: String): Int {
        return itemRepository.deleteById(id)
    }
}
