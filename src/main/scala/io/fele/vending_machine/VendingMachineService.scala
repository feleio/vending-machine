package io.fele.vending_machine

import io.fele.vending_machine.model.{CoinCount, Product, ProductCount, ReturnedProductAndChange}
import io.fele.vending_machine.repo.{CoinsRepo, ProductsRepo, VendingStateRepo}
import io.fele.vending_machine.ChangeCalculation.RichCoinCountList

sealed trait VendingMachineException extends Exception
case object ProductNotFoundException extends VendingMachineException
case object NotEnoughCoinsForChange extends VendingMachineException
case object NotEnoughCoinInserted extends VendingMachineException

trait VendingMachineService {
  // read only actions
  def listAvailableCoins: List[CoinCount]

  def listAvailableProduct: List[ProductCount]

  def getSelectedProductId: Option[Int]

  def getProduct(productId: Int): Option[Product]

  def getInsertedCoins: List[CoinCount]

  // actions with side effect
  def loadCoins(coinCounts: List[CoinCount]): Unit

  def loadProducts(products: List[ProductCount]): Unit

  def selectProduct(productId: Int): Either[VendingMachineException, Option[ReturnedProductAndChange]]

  def insertCoins(coinCounts: List[CoinCount]): Either[VendingMachineException, Option[ReturnedProductAndChange]]
}

class VendingMachineServiceImpl(
  val coinsRepo: CoinsRepo,
  val productsRepo: ProductsRepo,
  val vendingStateRepo: VendingStateRepo,
) extends VendingMachineService {
  override def listAvailableCoins: List[CoinCount] =
    coinsRepo.listCoins

  override def listAvailableProduct: List[ProductCount] =
    productsRepo.listProductCounts

  override def getSelectedProductId: Option[Int] =
    vendingStateRepo.getSelectedProduct

  override def getProduct(productId: Int): Option[Product] =
    productsRepo.getProduct(productId)

  override def getInsertedCoins: List[CoinCount] =
    vendingStateRepo.getInsertedCoins

  override def loadCoins(coinCounts: List[CoinCount]): Unit =
    coinsRepo.loadCoins(coinCounts)

  override def loadProducts(products: List[ProductCount]): Unit =
    productsRepo.loadProducts(products)

  override def selectProduct(productId: Int): Either[VendingMachineException, Option[ReturnedProductAndChange]] = {
    val productOpt: Option[Product] = getProduct(productId)

    productOpt match {
      case Some(product) =>
        vendingStateRepo.selectProduct(product.id)
        outputProductAndChange()
      case None =>
        Left(ProductNotFoundException)
    }
  }

  override def insertCoins(coinCounts: List[CoinCount]): Either[VendingMachineException, Option[ReturnedProductAndChange]] = {
    vendingStateRepo.insertCoins(coinCounts)
    outputProductAndChange()
  }

  private def outputProductAndChange(): Either[VendingMachineException, Option[ReturnedProductAndChange]] = {
    val selectedProduct: Option[Product] = getSelectedProductId.flatMap(getProduct)
    val insertedCoins: List[CoinCount] = getInsertedCoins
    val currentAmount: Int = insertedCoins.totalAmount

    selectedProduct match {
      case Some(product) =>
        if (currentAmount >= product.price) {
          coinsRepo.loadCoins(insertedCoins)
          val changeOpt: Option[List[CoinCount]] = ChangeCalculation.getChange(listAvailableCoins, currentAmount - product.price)
          changeOpt match {
            case Some(change) =>
              productsRepo.removeProduct(product.id)
              coinsRepo.removeCoins(change)
              vendingStateRepo.clear()
              val output = ReturnedProductAndChange(product, change)
              Right(Some(output))
            case None =>
              coinsRepo.removeCoins(insertedCoins)
              Left(NotEnoughCoinsForChange)
          }
        }
        else
          Left(NotEnoughCoinInserted)
      case None =>
        Right(None)
    }
  }
}