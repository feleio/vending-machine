package io.fele.vending_machine.repo

import io.fele.vending_machine.model.ProductCount

trait ProductsRepo {
  def getProducts: List[ProductCount]
  def loadProducts(productCounts: List[ProductCount]): Unit
}
