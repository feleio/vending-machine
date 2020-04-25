package io.fele.vending_machine.repo

import io.fele.vending_machine.model.ProductCount

sealed trait ProductException extends Exception
case object ProductNotFoundException extends ProductException

trait ProductsRepo {
  def listProducts: List[ProductCount]
  def getProductCount(productId: Int): ProductCount
  def loadProducts(productCounts: List[ProductCount]): Unit
  def removeProduct(productId: Int): Either[ProductException, Unit]
}

class InMemoryProductsRepo extends ProductsRepo {
  override def listProducts: List[ProductCount] = ???

  override def getProductCount(productId: Int): ProductCount = ???

  override def loadProducts(productCounts: List[ProductCount]): Unit = ???

  override def removeProduct(productId: Int): Either[ProductException, Unit] = ???
}

