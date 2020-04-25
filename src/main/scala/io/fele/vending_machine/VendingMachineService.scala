package io.fele.vending_machine

import io.fele.vending_machine.model.{CoinCount, ProductCount}

trait VendingMachineService {
  def getAvailableCoins: List[CoinCount]

  def getAvailableProduct: List[ProductCount]

  def getSelectedProduct: List[Product]

  def loadCoins(coinCounts: List[CoinCount]): Unit

  def loadProducts(products: List[ProductCount]): Unit

  def selectProduct(productId: Int): Unit

  def insertCoins(coinCounts: List[CoinCount]): Unit
}
