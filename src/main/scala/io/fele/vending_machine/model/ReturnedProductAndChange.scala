package io.fele.vending_machine.model

case class ReturnedProductAndChange(product: Product, change: List[CoinCount])
