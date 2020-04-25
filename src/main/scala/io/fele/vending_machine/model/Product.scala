package io.fele.vending_machine.model

// value unit pence
case class Product(id: Int, name: String)
case class ProductCount(product: Product, count: Int)