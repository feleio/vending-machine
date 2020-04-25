package io.fele.vending_machine.repo

import io.fele.vending_machine.model.{Coin, CoinCount}

import scala.collection.mutable

sealed trait CoinsException
case object NotEnoughCoinsException extends CoinsException


trait CoinsRepo {
  def getCoins: List[CoinCount]
  def loadCoins(coinCounts: List[CoinCount]): Unit
  def removeCoins(coinCounts: List[CoinCount]): Either[CoinsException, Unit]
}

class InMemoryCoinsRepo extends CoinsRepo {
  val coinsCountMap : mutable.HashMap[Int, Int] = mutable.HashMap(
    Coin.allCoins.map { value =>
      value -> 0
    }.toSeq:_*
  )

  override def getCoins: List[CoinCount] = coinsCountMap.collect {
    case (value, count) if count > 0 => CoinCount(Coin(value), count)
  }.toList.sortBy(_.coin.value)(Ordering.Int.reverse)

  override def loadCoins(coinCounts: List[CoinCount]): Unit = {
    for {
      coinCount <- coinCounts
    } {
      coinsCountMap(coinCount.coin.value) += coinCount.count
    }
  }

  override def removeCoins(coinCounts: List[CoinCount]): Either[CoinsException, Unit] = {
    val notEnoughCoinCounts = coinCounts.find { coinCount =>
      val curCount = coinsCountMap(coinCount.coin.value)
      curCount < coinCount.count
    }

    if(notEnoughCoinCounts.isDefined)
      Left(NotEnoughCoinsException)
    else {
      for {
        coinCount <- coinCounts
      } {
        val curCount = coinsCountMap(coinCount.coin.value)
        if(curCount >= coinCount.count)
          coinsCountMap(coinCount.coin.value) -= coinCount.count
        else
          return Left(NotEnoughCoinsException)
      }
      Right(())
    }
  }
}

