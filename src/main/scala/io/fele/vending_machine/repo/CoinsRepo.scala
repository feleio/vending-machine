package io.fele.vending_machine.repo

import io.fele.vending_machine.model.CoinCount

trait CoinsRepo {
  def getCoins: List[CoinCount]
  def loadCoins(coinCounts: List[CoinCount]): Unit
}

class InMemoryCoinsRepo extends CoinsRepo {
  override def getCoins: List[CoinCount] = ???

  override def loadCoins(coinCounts: List[CoinCount]): Unit = ???
}

