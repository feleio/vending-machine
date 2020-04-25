package io.fele.vending_machine.repo

import io.fele.vending_machine.model.ProductCount

trait ProductsRepo {
  def listProducts: List[ProductCount]
  def getProductCount(productId: Int): ProductCount
  def loadProducts(productCounts: List[ProductCount]): Unit
}
