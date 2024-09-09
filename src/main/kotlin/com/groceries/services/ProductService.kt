package com.groceries.services

import com.groceries.models.Invoice
import com.groceries.models.Product
import com.groceries.repositories.ProductRepository
import com.groceries.vo.InvoiceProductsRequest
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.*

@Service
class ProductService(
    val productRepository: ProductRepository,
    val priceService: PriceService
) {

    fun getProducts(): List<Product> {
        return productRepository.findAll()
    }

    fun getProduct(id: String): Product {
        return productRepository.findById(UUID.fromString(id)).orElseThrow { throw RuntimeException("Product not found") }
    }

    fun createProduct(product: Product): Product {
        return productRepository.save(product)
    }

    @Transactional
    fun processProduct(productRequest: InvoiceProductsRequest, invoice: Invoice): Product{
        val productOptional = productRepository.findBySku(productRequest.sku)

        val product = if (productOptional.isPresent) {
            val product = productOptional.get()
            if (product.bestPrice > productRequest.price) {
                updateProductBestPrice(product, productRequest.price)
            }
            product
        } else {
            createProduct(Product(
                sku = productRequest.sku,
                name = productRequest.name,
                bestPrice = productRequest.price
            ))
        }

        priceService.createPrice(product, invoice)
        return product
    }

    private fun updateProductBestPrice(product: Product, price: BigDecimal) {
        productRepository.save(product.copy(
            bestPrice = price,
            updatedAt = Timestamp(System.currentTimeMillis())
        ))
    }
}