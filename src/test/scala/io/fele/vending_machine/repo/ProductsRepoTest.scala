package io.fele.vending_machine.repo

import io.fele.vending_machine.model.{Product, ProductCount}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class ProductsRepoTest extends AnyFreeSpec with Matchers {
  val product1: Product = Product(1, "coke", 2900)
  val product2: Product = Product(2, "milk", 900)
  val product3: Product = Product(3, "wine", 8900)

  val products = List(
    ProductCount(product = product1, 100),
    ProductCount(product = product2, 100),
    ProductCount(product = product3, 100),
  )

  val zeroProductsRepo = new InMemoryProductsRepo()
  val oneProductsRepo = new InMemoryProductsRepo()
  val productsRepo = new InMemoryProductsRepo()
  val doubleProductsRepo = new InMemoryProductsRepo()
  productsRepo.loadProducts(products)
  oneProductsRepo.loadProducts(List(
    ProductCount(product = product1, 1),
    ProductCount(product = product2, 1),
    ProductCount(product = product3, 1),
  ))
  doubleProductsRepo.loadProducts(products)
  doubleProductsRepo.loadProducts(products)

  "ProductsRepo" - {
    "when contains 0 products" - {
      zeroProductsRepo.listProductCounts should be (Nil)

      "should be able to load 0 products" in {
        val zeroProducts = Nil
        zeroProductsRepo.loadProducts(zeroProducts)
        zeroProductsRepo.listProductCounts should be (Nil)
      }

      "should be able to load products" in {
        zeroProductsRepo.loadProducts(products)
        zeroProductsRepo.listProductCounts should be (products)
      }
    }

    "when contains many products" - {
      productsRepo.getProduct(productId = 1) should be (Some(product1))
      productsRepo.getProduct(productId = 2) should be (Some(product2))
      productsRepo.getProduct(productId = 3) should be (Some(product3))
      productsRepo.listProductCounts should be (products)

      "should be able to load 0 products" in {
        val zeroProducts = Nil
        productsRepo.loadProducts(zeroProducts)
        productsRepo.listProductCounts should be (products)
      }

      "should be able to load products" in {
        productsRepo.loadProducts(products)
        productsRepo.listProductCounts should be (List(
          ProductCount(product = product1, 200),
          ProductCount(product = product2, 200),
          ProductCount(product = product3, 200),
        ))
        productsRepo.getProductCount(productId = 1) should be (200)
        productsRepo.getProductCount(productId = 2) should be (200)
        productsRepo.getProductCount(productId = 3) should be (200)
      }

      "should be able to remove products" in {
        doubleProductsRepo.removeProduct(productId = 1) should be (Right(()))
        doubleProductsRepo.removeProduct(productId = 2) should be (Right(()))
        doubleProductsRepo.removeProduct(productId = 3) should be (Right(()))
        doubleProductsRepo.listProductCounts should be (List(
          ProductCount(product = product1, 199),
          ProductCount(product = product2, 199),
          ProductCount(product = product3, 199),
        ))
        doubleProductsRepo.getProductCount(productId = 1) should be (199)
        doubleProductsRepo.getProductCount(productId = 2) should be (199)
        doubleProductsRepo.getProductCount(productId = 3) should be (199)
      }

      "given if not enough products to be removed" - {
        "should not able to remove products" in {
          oneProductsRepo.listProductCounts should be (List(
            ProductCount(product = product1, 1),
            ProductCount(product = product2, 1),
            ProductCount(product = product3, 1),
          ))
          oneProductsRepo.removeProduct(productId = 1) should be (Right(()))
          oneProductsRepo.listProductCounts should be (List(
            ProductCount(product = product2, 1),
            ProductCount(product = product3, 1),
          ))
          oneProductsRepo.removeProduct(productId = 1) should be (Left(ProductNotFoundException))
          oneProductsRepo.listProductCounts should be (List(
            ProductCount(product = product2, 1),
            ProductCount(product = product3, 1),
          ))

          oneProductsRepo.getProductCount(productId = 1) should be (0)
          oneProductsRepo.getProductCount(productId = 2) should be (1)
          oneProductsRepo.getProductCount(productId = 3) should be (1)
        }
      }
    }
  }
}
