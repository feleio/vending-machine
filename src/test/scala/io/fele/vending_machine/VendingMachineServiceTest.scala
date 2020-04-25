package io.fele.vending_machine

import io.fele.vending_machine.model.{Coin, CoinCount, Product, ProductCount}
import io.fele.vending_machine.repo.{CoinsRepo, InMemoryCoinsRepo, InMemoryProductsRepo, InMemoryVendingStateRepo, ProductsRepo, VendingStateRepo}
import org.scalatest.BeforeAndAfter
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class VendingMachineServiceTest extends AnyFunSpec with Matchers with BeforeAndAfter {
  var coinsRepo: CoinsRepo = _
  var productsRepo: ProductsRepo = _
  var vendingStateRepo: VendingStateRepo = _
  var emptyVendingMachineService: VendingMachineService = _
  var vendingMachineService: VendingMachineService = _

  val coins = List(
    CoinCount(Coin(200), count = 100),
    CoinCount(Coin(100), count = 100),
    CoinCount(Coin(50), count = 100),
    CoinCount(Coin(20), count = 100),
    CoinCount(Coin(10), count = 100),
    CoinCount(Coin(5), count = 100),
    CoinCount(Coin(2), count = 100),
    CoinCount(Coin(1), count = 100),
  )

  val product1: Product = Product(1, "coke", 2900)
  val product2: Product = Product(2, "milk", 900)
  val product3: Product = Product(3, "wine", 8900)

  val products = List(
    ProductCount(product = product1, 100),
    ProductCount(product = product2, 100),
    ProductCount(product = product3, 100),
  )

  before {
    coinsRepo = new InMemoryCoinsRepo()
    productsRepo = new InMemoryProductsRepo()
    vendingStateRepo = new InMemoryVendingStateRepo()
    emptyVendingMachineService = new VendingMachineServiceImpl(coinsRepo, productsRepo, vendingStateRepo)
    vendingMachineService = new VendingMachineServiceImpl(coinsRepo, productsRepo, vendingStateRepo)

  }

  describe("VendingMachineService") {
    it("Empty Vending machine should be able to load coins and product") {
      emptyVendingMachineService.listAvailableCoins should be (Nil)
      emptyVendingMachineService.loadCoins(coins)
      emptyVendingMachineService.listAvailableCoins should be (coins)

      emptyVendingMachineService.listAvailableProduct should be (Nil)
      emptyVendingMachineService.loadProducts(products)
      emptyVendingMachineService.listAvailableProduct should be (products)
    }

    it("Empty Vending machine should be able to load coins and product2") {
      emptyVendingMachineService.listAvailableCoins should be (Nil)
      emptyVendingMachineService.loadCoins(coins)
      emptyVendingMachineService.listAvailableCoins should be (coins)

      emptyVendingMachineService.listAvailableProduct should be (Nil)
      emptyVendingMachineService.loadProducts(products)
      emptyVendingMachineService.listAvailableProduct should be (products)
    }
  }

}
