package io.fele.vending_machine

import io.fele.vending_machine.model.{Coin, CoinCount, Product, ProductCount, ReturnedProductAndChange}
import io.fele.vending_machine.repo.{CoinsRepo, InMemoryCoinsRepo, InMemoryProductsRepo, InMemoryVendingStateRepo, ProductsRepo, VendingStateRepo}
import io.fele.vending_machine.ChangeCalculation.RichCoinCountList
import org.scalatest.BeforeAndAfter
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class VendingMachineServiceTest extends AnyFunSpec with Matchers with BeforeAndAfter {
  var coinsRepo: CoinsRepo = _
  var productsRepo: ProductsRepo = _
  var vendingStateRepo: VendingStateRepo = _
  var emptyVendingMachineService: VendingMachineService = _
  var vendingMachineService: VendingMachineService = _
  var limitedChangeVendingMachineService: VendingMachineService = _
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
  val limitedCoins = List(
    CoinCount(Coin(200), count = 1),
    CoinCount(Coin(100), count = 1),
  )

  val product1: Product = Product(id = 1, name = "coke", price = 290)
  val product2: Product = Product(id = 2, name = "milk", price = 90)
  val product3: Product = Product(id = 3, name = "wine", price = 890)

  val products = List(
    ProductCount(product = product1, count = 10),
    ProductCount(product = product2, count = 10),
    ProductCount(product = product3, count = 10),
  )

  before {
    def coinsRepo = new InMemoryCoinsRepo()
    def productsRepo = new InMemoryProductsRepo()
    def vendingStateRepo = new InMemoryVendingStateRepo()
    emptyVendingMachineService = new VendingMachineServiceImpl(coinsRepo, productsRepo, vendingStateRepo)
    vendingMachineService = new VendingMachineServiceImpl(coinsRepo, productsRepo, vendingStateRepo)
    vendingMachineService.loadCoins(coins)
    vendingMachineService.loadProducts(products)
    limitedChangeVendingMachineService = new VendingMachineServiceImpl(coinsRepo, productsRepo, vendingStateRepo)
    limitedChangeVendingMachineService.loadProducts(products)
    limitedChangeVendingMachineService.loadCoins(limitedCoins)
  }

  describe("Empty VendingMachineService") {
    it("should be able to load coins and product") {
      emptyVendingMachineService.listAvailableCoins should be (Nil)
      emptyVendingMachineService.loadCoins(coins)
      emptyVendingMachineService.listAvailableCoins should be (coins)

      emptyVendingMachineService.listAvailableProduct should be (Nil)
      emptyVendingMachineService.loadProducts(products)
      emptyVendingMachineService.listAvailableProduct should be (products)
    }
  }

  describe("Loaded VendingMachineService") {
    it("when select product after inserting coins, should be able to output a product") {
      vendingMachineService.listAvailableProduct should be (List(
        ProductCount(product = product1, count = 10),
        ProductCount(product = product2, count = 10),
        ProductCount(product = product3, count = 10),
      ))
      vendingMachineService.getProduct(product1.id) should be (
        Some(Product(id = 1, name = "coke", price = 290))
      )

      vendingMachineService.insertCoins(List(
        CoinCount(Coin(100), 2),
        CoinCount(Coin(200), 1),
      )) should be (
        Right(None)
      )

      vendingMachineService.getInsertedCoins should be (List(
        CoinCount(Coin(200), 1),
        CoinCount(Coin(100), 2),
      ))


      vendingMachineService.listAvailableCoins.totalAmount should be (38800)
      vendingMachineService.listAvailableCoins should be (List(
        CoinCount(Coin(200), count = 100),
        CoinCount(Coin(100), count = 100),
        CoinCount(Coin(50), count = 100),
        CoinCount(Coin(20), count = 100),
        CoinCount(Coin(10), count = 100),
        CoinCount(Coin(5), count = 100),
        CoinCount(Coin(2), count = 100),
        CoinCount(Coin(1), count = 100),
      ))

      val expectedOutput =  Right(Some(ReturnedProductAndChange(product = product1, change = List(
        CoinCount(Coin(100), 1),
        CoinCount(Coin(10), 1),
      ))))

      vendingMachineService.selectProduct(product1.id) should be (expectedOutput)

      vendingMachineService.listAvailableCoins.totalAmount should be (38800 + 290)
      vendingMachineService.listAvailableCoins should be (List(
        CoinCount(Coin(200), count = 100 + 1),
        CoinCount(Coin(100), count = 100 - 1 + 2),
        CoinCount(Coin(50), count = 100),
        CoinCount(Coin(20), count = 100),
        CoinCount(Coin(10), count = 100 - 1),
        CoinCount(Coin(5), count = 100),
        CoinCount(Coin(2), count = 100),
        CoinCount(Coin(1), count = 100),
      ))

      vendingMachineService.listAvailableProduct should be (List(
        ProductCount(product = product1, count = 10 - 1),
        ProductCount(product = product2, count = 10),
        ProductCount(product = product3, count = 10),
      ))

      vendingMachineService.getSelectedProductId should be (None)
      vendingMachineService.getInsertedCoins should be (Nil)
    }

    it("when select product without enough coin, should not be able to output a product") {
      vendingMachineService.listAvailableProduct should be (List(
        ProductCount(product = product1, count = 10),
        ProductCount(product = product2, count = 10),
        ProductCount(product = product3, count = 10),
      ))
      vendingMachineService.getProduct(product3.id) should be (
        Some(Product(id = 3, name = "wine", price = 890))
      )

      vendingMachineService.insertCoins(List(
        CoinCount(Coin(100), 2),
        CoinCount(Coin(200), 1),
      )) should be (
        Right(None)
      )

      vendingMachineService.getInsertedCoins should be (List(
        CoinCount(Coin(200), 1),
        CoinCount(Coin(100), 2),
      ))

      vendingMachineService.listAvailableCoins.totalAmount should be (38800)
      vendingMachineService.listAvailableCoins should be (List(
        CoinCount(Coin(200), count = 100),
        CoinCount(Coin(100), count = 100),
        CoinCount(Coin(50), count = 100),
        CoinCount(Coin(20), count = 100),
        CoinCount(Coin(10), count = 100),
        CoinCount(Coin(5), count = 100),
        CoinCount(Coin(2), count = 100),
        CoinCount(Coin(1), count = 100),
      ))

      val expectedOutput = Left(NotEnoughCoinInsertedException)

      vendingMachineService.selectProduct(product3.id) should be (expectedOutput)

      vendingMachineService.listAvailableCoins.totalAmount should be (38800)
      vendingMachineService.listAvailableCoins should be (List(
        CoinCount(Coin(200), count = 100),
        CoinCount(Coin(100), count = 100),
        CoinCount(Coin(50), count = 100),
        CoinCount(Coin(20), count = 100),
        CoinCount(Coin(10), count = 100),
        CoinCount(Coin(5), count = 100),
        CoinCount(Coin(2), count = 100),
        CoinCount(Coin(1), count = 100),
      ))

      vendingMachineService.listAvailableProduct should be (List(
        ProductCount(product = product1, count = 10),
        ProductCount(product = product2, count = 10),
        ProductCount(product = product3, count = 10),
      ))

      vendingMachineService.getSelectedProductId should be (Some(3))
      vendingMachineService.getInsertedCoins should be (List(
        CoinCount(Coin(200), 1),
        CoinCount(Coin(100), 2),
      ))
    }

    it("when insert enough coins after complaint not enough coin, should be able to output a product") {
      vendingMachineService.listAvailableProduct should be (List(
        ProductCount(product = product1, count = 10),
        ProductCount(product = product2, count = 10),
        ProductCount(product = product3, count = 10),
      ))
      vendingMachineService.getProduct(product3.id) should be (
        Some(Product(id = 3, name = "wine", price = 890))
      )

      vendingMachineService.insertCoins(List(
        CoinCount(Coin(100), 2),
        CoinCount(Coin(200), 1),
      )) should be (
        Right(None)
      )

      vendingMachineService.getInsertedCoins should be (List(
        CoinCount(Coin(200), 1),
        CoinCount(Coin(100), 2),
      ))

      vendingMachineService.listAvailableCoins.totalAmount should be (38800)
      vendingMachineService.listAvailableCoins should be (List(
        CoinCount(Coin(200), count = 100),
        CoinCount(Coin(100), count = 100),
        CoinCount(Coin(50), count = 100),
        CoinCount(Coin(20), count = 100),
        CoinCount(Coin(10), count = 100),
        CoinCount(Coin(5), count = 100),
        CoinCount(Coin(2), count = 100),
        CoinCount(Coin(1), count = 100),
      ))

      vendingMachineService.selectProduct(product3.id) should be (Left(NotEnoughCoinInsertedException))

      val expectedOutput =  Right(Some(ReturnedProductAndChange(product = product3, change = List(
        CoinCount(Coin(200), 2),
        CoinCount(Coin(100), 1),
        CoinCount(Coin(10), 1),
      ))))

      vendingMachineService.insertCoins(List(
        CoinCount(Coin(200), 5),
      )) should be (expectedOutput)

      vendingMachineService.listAvailableCoins.totalAmount should be (38800 + 890)
      vendingMachineService.listAvailableCoins should be (List(
        CoinCount(Coin(200), count = 100 - 2 + 6),
        CoinCount(Coin(100), count = 100 - 1 + 2),
        CoinCount(Coin(50), count = 100),
        CoinCount(Coin(20), count = 100),
        CoinCount(Coin(10), count = 100 - 1),
        CoinCount(Coin(5), count = 100),
        CoinCount(Coin(2), count = 100),
        CoinCount(Coin(1), count = 100),
      ))

      vendingMachineService.listAvailableProduct should be (List(
        ProductCount(product = product1, count = 10),
        ProductCount(product = product2, count = 10),
        ProductCount(product = product3, count = 10 - 1),
      ))

      vendingMachineService.getSelectedProductId should be (None)
      vendingMachineService.getInsertedCoins should be (Nil)
    }
  }


  describe("Limited change VendingMachineService") {
    it("should not be able to output a product because not enough change") {
      limitedChangeVendingMachineService.listAvailableProduct should be(List(
        ProductCount(product = product1, count = 10),
        ProductCount(product = product2, count = 10),
        ProductCount(product = product3, count = 10),
      ))
      limitedChangeVendingMachineService.getProduct(product2.id) should be(
        Some(Product(id = 2, name = "milk", price = 90))
      )

      limitedChangeVendingMachineService.insertCoins(List(
        CoinCount(Coin(200), 1),
      )) should be(
        Right(None)
      )

      limitedChangeVendingMachineService.getInsertedCoins should be(List(
        CoinCount(Coin(200), 1),
      ))

      limitedChangeVendingMachineService.listAvailableCoins.totalAmount should be(300)
      limitedChangeVendingMachineService.listAvailableCoins should be(List(
        CoinCount(Coin(200), count = 1),
        CoinCount(Coin(100), count = 1),
      ))

      val expectedOutput = Left(NotEnoughCoinsForChangeException)
      limitedChangeVendingMachineService.selectProduct(product2.id) should be(expectedOutput)

      limitedChangeVendingMachineService.listAvailableCoins.totalAmount should be(300)
      limitedChangeVendingMachineService.listAvailableCoins should be(List(
        CoinCount(Coin(200), count = 1),
        CoinCount(Coin(100), count = 1),
      ))

      limitedChangeVendingMachineService.listAvailableProduct should be(List(
        ProductCount(product = product1, count = 10),
        ProductCount(product = product2, count = 10),
        ProductCount(product = product3, count = 10),
      ))

      limitedChangeVendingMachineService.getSelectedProductId should be(Some(2))
      limitedChangeVendingMachineService.getInsertedCoins should be(List(
        CoinCount(Coin(200), 1),
      ))
    }
  }

}
