package com.example.vendor_item.service
import com.example.vendor_item.model.ListVendor
import com.example.vendor_item.model.Vendor
import com.example.vendor_item.repository.VendorRepository
import org.springframework.stereotype.Service

@Service
class VendorService(private val vendorRepository: VendorRepository) {

    fun getAllVendors(listVendor: ListVendor): List<Vendor> {
        return vendorRepository.findAll(listVendor.search, listVendor.page, listVendor.size, listVendor.getAll)
    }

    fun getVendorById(id: String): Vendor? {
        return vendorRepository.findById(id)
    }

    fun createVendor(vendor: Vendor): Int {
        return vendorRepository.save(vendor)
    }

    fun updateVendor(vendor: Vendor): Int {
        return vendorRepository.update(vendor)
    }

    fun deleteVendor(id: String): Int {
        return vendorRepository.deleteById(id)
    }
}
