package io.fele.vending_machine.repo

import io.fele.vending_machine.model.{Coin, ProductCount}

import scala.collection.mutable

sealed trait ProductException extends Exception
case object ProductNotFoundException extends ProductException

trait ProductsRepo {
  def listProducts: List[ProductCount]
  def getProductCount(productId: Int): Int
  def loadProducts(productCounts: List[ProductCount]): Unit
  def removeProduct(productId: Int): Either[ProductException, Unit]
}

class InMemoryProductsRepo extends ProductsRepo {
  val productCountMap : mutable.HashMap[Int, Int] = mutable.HashMap()

  override def listProducts: List[ProductCount] = productCountMap.collect {
    case (productId, count) if count > 0 => ProductCount(productId, count)
  }.toList.sortBy(_.productId)

  override def getProductCount(productId: Int): Int =
    productCountMap.getOrElse(productId, 0)

  override def loadProducts(productCounts: List[ProductCount]): Unit = {
    for {
      productCount <- productCounts
    } {
      productCountMap.get(productCount.productId) match {
        case Some(_) =>
          productCountMap(productCount.productId) += productCount.count
        case None =>
          productCountMap(productCount.productId) = productCount.count
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

