package io.fele.vending_machine

import io.fele.vending_machine.model.{CoinCount, ProductCount}

trait VendingMachineService {
  // read only actions
  def listAvailableCoins: List[CoinCount]

  def listAvailableProduct: List[ProductCount]

  def getSelectedProduct: Option[Product]

  // actions with side effect
  def loadCoins(coinCounts: List[CoinCount]): Unit

  def loadProducts(products: List[ProductCount]): Unit

  def selectProduct(productId: Int): Unit

  def insertCoins(coinCounts: List[CoinCount]): Unit

  def returnCoins(): List[CoinCount]
}
