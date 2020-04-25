package io.fele.vending_machine

import scala.math.min
import io.fele.vending_machine.model.{Coin, CoinCount}

// core algorithm to calculate coin for change
object ChangeCalculation {
  implicit class RichCoinCountList(coinCounts: List[CoinCount]) {
    def totalAmount: Int = coinCounts.foldLeft(0)((total, coinCount) => total + coinCount.coin.value * coinCount.count)
  }

  def getChange(availableCoins: List[CoinCount], changeAmount: Int): Option[List[CoinCount]] = {
    if (availableCoins.totalAmount >= changeAmount)
      getChangeRec(availableCoins, changeAmount)
    else None
  }

  private def getChangeRec(availableCoins: List[CoinCount], changeAmount: Int): Option[List[CoinCount]] = {
    if (changeAmount > 0) {
      if (availableCoins.nonEmpty) {
        val coinCount = availableCoins.head
        val canTakeMost = min(changeAmount / coinCount.coin.value, coinCount.count)
        for {
          curCoinCount <- canTakeMost to 0 by -1
        } {
          val amountDeducted: Int = changeAmount - coinCount.coin.value * curCoinCount
          val subResultOpt: Option[List[CoinCount]] = getChange(availableCoins.tail, amountDeducted)
          val result: Option[List[CoinCount]] = subResultOpt.map { subResult =>
            if (curCoinCount > 0)
              CoinCount(coinCount.coin, curCoinCount):: subResult
            else
              subResult
          }

          if (result.isDefined)
            return result
        }
        None
      } else None
    } else Some(Nil)
  }
}