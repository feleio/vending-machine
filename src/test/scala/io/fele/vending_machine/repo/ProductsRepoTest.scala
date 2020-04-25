package io.fele.vending_machine.repo

import io.fele.vending_machine.model.{Coin, CoinCount, Product, ProductCount}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class ProductsRepoTest extends AnyFreeSpec with Matchers {
  val products = List(
    ProductCount(productId = 1, 100),
    ProductCount(productId = 2, 100),
    ProductCount(productId = 3, 100),
  )

  val zeroProductsRepo = new InMemoryProductsRepo()
  val oneProductsRepo = new InMemoryProductsRepo()
  val productsRepo = new InMemoryProductsRepo()
  val doubleProductsRepo = new InMemoryProductsRepo()
  productsRepo.loadProducts(products)
  oneProductsRepo.loadProducts(List(
    ProductCount(productId = 1, 1),
    ProductCount(productId = 2, 1),
    ProductCount(productId = 3, 1),
  ))
  doubleProductsRepo.loadProducts(products)
  doubleProductsRepo.loadProducts(products)

  "ProductsRepo" - {
    "when contains 0 products" - {
      zeroProductsRepo.listProducts should be (Nil)

      "should be able to load 0 products" in {
        val zeroProducts = Nil
        zeroProductsRepo.loadProducts(zeroProducts)
        zeroProductsRepo.listProducts should be (Nil)
      }

      "should be able to load products" in {
        zeroProductsRepo.loadProducts(products)
        zeroProductsRepo.listProducts should be (products)
      }
    }

    "when contains many products" - {
      productsRepo.listProducts should be (products)

      "should be able to load 0 products" in {
        val zeroProducts = Nil
        productsRepo.loadProducts(zeroProducts)
        productsRepo.listProducts should be (products)
      }

      "should be able to load products" in {
        productsRepo.loadProducts(products)
        productsRepo.listProducts should be (List(
          ProductCount(productId = 1, 200),
          ProductCount(productId = 2, 200),
          ProductCount(productId = 3, 200),
        ))
      }

      "should be able to remove products" in {
        doubleProductsRepo.removeProduct(1) should be (Right(()))
        doubleProductsRepo.removeProduct(2) should be (Right(()))
        doubleProductsRepo.removeProduct(3) should be (Right(()))
        doubleProductsRepo.listProducts should be (List(
          ProductCount(productId = 1, 199),
          ProductCount(productId = 2, 199),
          ProductCount(productId = 3, 199),
        ))
      }

      "given if not enough products to be removed" - {
        "should not able to remove coins" in {
          oneProductsRepo.listProducts should be (List(
            ProductCount(productId = 1, 1),
            ProductCount(productId = 2, 1),
            ProductCount(productId = 3, 1),
          ))
          oneProductsRepo.removeProduct(1) should be (Right(()))
          oneProductsRepo.listProducts should be (List(
            ProductCount(productId = 1, 0),
            ProductCount(productId = 2, 1),
            ProductCount(productId = 3, 1),
          ))
          oneProductsRepo.removeProduct(1) should be (Left(ProductNotFoundException))
          oneProductsRepo.listProducts should be (List(
            ProductCount(productId = 1, 0),
            ProductCount(productId = 2, 1),
            ProductCount(productId = 3, 1),
          ))
        }
      }
    }
  }
}
