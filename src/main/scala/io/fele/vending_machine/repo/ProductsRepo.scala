package io.fele.vending_machine.repo

import io.fele.vending_machine.model.{Product, ProductCount}

import scala.collection.mutable

sealed trait ProductException extends Exception
case object ProductNotFoundException extends ProductException

trait ProductsRepo {
  def getProduct(productId: Int): Option[Product]
  def listProductCounts: List[ProductCount]
  def getProductCount(productId: Int): Int
  def loadProducts(productCounts: List[ProductCount]): Unit
  def removeProduct(productId: Int): Either[ProductException, Unit]
}

class InMemoryProductsRepo extends ProductsRepo {
  val productMap : mutable.HashMap[Int, Product] = mutable.HashMap()
  val productCountMap : mutable.HashMap[Int, Int] = mutable.HashMap()

  override def getProduct(productId: Int): Option[Product] = productMap.get(productId)

  override def listProductCounts: List[ProductCount] = productCountMap.collect {
    case (productId, count) if count > 0 => ProductCount(productMap(productId), count)
  }.toList.sortBy(_.product.id)

  override def getProductCount(productId: Int): Int =
    productCountMap.getOrElse(productId, 0)

  override def loadProducts(productCounts: List[ProductCount]): Unit = {
    for {
      productCount <- productCounts
    } {
      productMap(productCount.product.id) = productCount.product
      productCountMap.get(productCount.product.id) match {
        case Some(_) =>
          productCountMap(productCount.product.id) += productCount.count
        case None =>
          productCountMap(productCount.product.id) = productCount.count
      }
    }
  }

  override def removeProduct(productId: Int): Either[ProductException, Unit] =
    if (productCountMap.getOrElse(productId, 0) > 0) {
      productCountMap(productId) -= 1
      Right(())
    }
    else
      Left(ProductNotFoundException)
}

