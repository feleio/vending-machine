package io.fele.vending_machine.repo

import io.fele.vending_machine.model.CoinCount

sealed trait CoinsException
case object NotEnoughCoinsException extends Exception


trait CoinsRepo {
  def getCoins: List[CoinCount]
  def loadCoins(coinCounts: List[CoinCount]): Unit
  def removeCoins(coinCounts: List[CoinCount]): Either[CoinsException, Unit]
}

class InMemoryCoinsRepo extends CoinsRepo {
  override def getCoins: List[CoinCount] = ???

  override def loadCoins(coinCounts: List[CoinCount]): Unit = ???

  override def removeCoins(coinCounts: List[CoinCount]): Either[CoinsException, Unit] = ???
}

