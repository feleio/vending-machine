package io.fele.vending_machine.repo

trait VendingStateRepo {
  def getInsertedCoin: List[CoinsRepo]
  def getSelectedProduct: Int

  def insertCoins(coins: List[CoinsRepo]): Unit
  def selectProduct(productId: Int): Unit
  def clear(): Unit
}

