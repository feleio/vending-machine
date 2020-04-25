package io.fele.vending_machine.repo

trait VendingMachineStateRepo {
  def getSelectedProductId: Option[Int]

  def setSelectedProductId(productId: Int): Unit

  def removeSelectedProductId(): Unit

  def getInsertedCoins: List[CoinsRepo]

  def insertCoin(coinCounts: List[CoinsRepo]): Unit

  def returnCoins(): Unit
}
