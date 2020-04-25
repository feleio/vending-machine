package io.fele.vending_machine.model

case class ReturnedProductAndChange(productId: Int, change: List[CoinCount])
