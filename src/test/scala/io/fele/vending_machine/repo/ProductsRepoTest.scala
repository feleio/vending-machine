package io.fele.vending_machine.repo

import io.fele.vending_machine.model.ProductCount
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
        productsRepo.getProductCount(productId = 1) should be (200)
        productsRepo.getProductCount(productId = 2) should be (200)
        productsRepo.getProductCount(productId = 3) should be (200)
      }

      "should be able to remove products" in {
        doubleProductsRepo.removeProduct(productId = 1) should be (Right(()))
        doubleProductsRepo.removeProduct(productId = 2) should be (Right(()))
        doubleProductsRepo.removeProduct(productId = 3) should be (Right(()))
        doubleProductsRepo.listProducts should be (List(
          ProductCount(productId = 1, 199),
          ProductCount(productId = 2, 199),
          ProductCount(productId = 3, 199),
        ))
        doubleProductsRepo.getProductCount(productId = 1) should be (199)
        doubleProductsRepo.getProductCount(productId = 2) should be (199)
        doubleProductsRepo.getProductCount(productId = 3) should be (199)
      }

      "given if not enough products to be removed" - {
        "should not able to remove products" in {
          oneProductsRepo.listProducts should be (List(
            ProductCount(productId = 1, 1),
            ProductCount(productId = 2, 1),
            ProductCount(productId = 3, 1),
          ))
          oneProductsRepo.removeProduct(productId = 1) should be (Right(()))
          oneProductsRepo.listProducts should be (List(
            ProductCount(productId = 2, 1),
            ProductCount(productId = 3, 1),
          ))
          oneProductsRepo.removeProduct(productId = 1) should be (Left(ProductNotFoundException))
          oneProductsRepo.listProducts should be (List(
            ProductCount(productId = 2, 1),
            ProductCount(productId = 3, 1),
          ))

          oneProductsRepo.getProductCount(productId = 1) should be (0)
          oneProductsRepo.getProductCount(productId = 2) should be (1)
          oneProductsRepo.getProductCount(productId = 3) should be (1)
        }
      }
    }
  }
}
