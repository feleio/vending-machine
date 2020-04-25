package io.fele.vending_machine

import io.fele.vending_machine.model.{CoinCount, ProductCount, ReturnedProductAndChange}

trait VendingMachineService {
  // read only actions
  def listAvailableCoins: List[CoinCount]

  def listAvailableProduct: List[ProductCount]

  def getSelectedProduct: Option[Int]

  def getInsertedCoins: List[CoinCount]

  // actions with side effect
  def loadCoins(coinCounts: List[CoinCount]): Unit

  def loadProducts(products: List[ProductCount]): Unit

  def selectProduct(productId: Int): Option[ReturnedProductAndChange]

  def insertCoin(coinCounts: List[CoinCount]): Option[ReturnedProductAndChange]
}
