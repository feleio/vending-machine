package io.fele.vending_machine.model

// value unit pence
case class Coin(value: Int)
case class CoinCount(coin: Coin, count: Int)

object Coin {
  val allCoins: Set[Int] = Set(1, 2, 5, 10, 20, 50, 100, 200)
}