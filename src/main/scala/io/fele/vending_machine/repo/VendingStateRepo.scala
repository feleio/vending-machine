package io.fele.vending_machine.repo

import io.fele.vending_machine.model.{Coin, CoinCount}

import scala.collection.mutable

trait VendingStateRepo {
  def getInsertedCoins: List[CoinCount]
  def getSelectedProduct: Option[Int]

  def insertCoins(coins: List[CoinCount]): Unit
  def selectProduct(productId: Int): Unit
  def clear(): Unit
}

class InMemoryVendingStateRepo extends VendingStateRepo {
  var currentProductId: Option[Int] = None
  val coinsCountMap : mutable.HashMap[Int, Int] = mutable.HashMap(
    Coin.allCoins.map { value =>
      value -> 0
    }.toSeq:_*
  )
  
  override def getInsertedCoins: List[CoinCount] = coinsCountMap.collect {
    case (value, count) if count > 0 => CoinCount(Coin(value), count)
  }.toList.sortBy(_.coin.value)(Ordering.Int.reverse)

  override def getSelectedProduct: Option[Int] = currentProductId

  override def insertCoins(coins: List[CoinCount]): Unit = {
    for {
      coinCount <- coins
    } {
      coinsCountMap(coinCount.coin.value) += coinCount.count
    }
  }

  override def selectProduct(productId: Int): Unit = {
    currentProductId = Some(productId)
  }

  override def clear(): Unit = {
    currentProductId = None
    coinsCountMap.clear()
  }
}