package io.fele.vending_machine.model

// value unit pence
case class Coin(value: Int)
case class CoinCount(coin: Coin, count: Int)