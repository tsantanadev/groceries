package com.groceries.models

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.UUID

@Entity
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    val sku: String,
    val name: String,
    val bestPrice: BigDecimal,

    @OneToMany(mappedBy = "product")
    val prices: List<Price> = emptyList(),
    @ManyToOne
    val bestPriceStore: Store? = null,

    val updatedAt: Timestamp = Timestamp(System.currentTimeMillis()),
    val createdAt: Timestamp = Timestamp(System.currentTimeMillis())
)
