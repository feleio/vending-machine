package io.fele.vending_machine.repo

import io.fele.vending_machine.model.CoinCount

trait CoinsRepo {
  def getCoins: List[CoinCount]
  def loadCoins(coinCounts: List[CoinCount]): Unit
}

