package io.fele.vending_machine.repo

import io.fele.vending_machine.model.{Coin, CoinCount}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class VendingStateRepoTest extends AnyFreeSpec with Matchers {
  val coins = List(
    CoinCount(Coin(200), count = 2),
    CoinCount(Coin(100), count = 2),
    CoinCount(Coin(50), count = 2),
    CoinCount(Coin(20), count = 2),
    CoinCount(Coin(10), count = 2),
    CoinCount(Coin(5), count = 2),
    CoinCount(Coin(2), count = 2),
    CoinCount(Coin(1), count = 2),
  )

  val vendingStateRepo = new InMemoryVendingStateRepo()
  val vendingStateRepoWithCoins = new InMemoryVendingStateRepo()
  val stateRepoForReset = new InMemoryVendingStateRepo()
  vendingStateRepoWithCoins.insertCoins(coins)
  stateRepoForReset.insertCoins(coins)
  stateRepoForReset.selectProduct(productId = 1)

  "VendingStateRepo" - {
    "when contains 0 coins and no selected Product" - {
      vendingStateRepo.getInsertedCoins should be (Nil)
      vendingStateRepo.getSelectedProduct should be (None)

      "should be able to insert 0 coins" in {
        val zeroCoins = Nil
        vendingStateRepo.insertCoins(zeroCoins)
        vendingStateRepo.getInsertedCoins should be (Nil)
      }

      "should be able to load coins" in {
        vendingStateRepo.insertCoins(coins)
        vendingStateRepo.getInsertedCoins should be (coins)
      }

      "should be able to select product" in {
        vendingStateRepo.getSelectedProduct should be (None)
        vendingStateRepo.selectProduct(productId = 1)
        vendingStateRepo.getSelectedProduct should be (Some(1))
        vendingStateRepo.selectProduct(productId = 2)
        vendingStateRepo.getSelectedProduct should be (Some(2))
      }
    }

    "when contains many coins" - {
      vendingStateRepoWithCoins.getInsertedCoins should be (coins)
      vendingStateRepoWithCoins.getSelectedProduct should be (None)

      "should be able to insert more coins" in {
        vendingStateRepoWithCoins.getInsertedCoins should be (coins)
        vendingStateRepoWithCoins.insertCoins(coins)
        vendingStateRepoWithCoins.getInsertedCoins should be (List(
          CoinCount(Coin(200), count = 4),
          CoinCount(Coin(100), count = 4),
          CoinCount(Coin(50), count = 4),
          CoinCount(Coin(20), count = 4),
          CoinCount(Coin(10), count = 4),
          CoinCount(Coin(5), count = 4),
          CoinCount(Coin(2), count = 4),
          CoinCount(Coin(1), count = 4),
        ))
        vendingStateRepoWithCoins.insertCoins(List(
          CoinCount(Coin(200), count = 1),
        ))
        vendingStateRepoWithCoins.getInsertedCoins should be (List(
          CoinCount(Coin(200), count = 5),
          CoinCount(Coin(100), count = 4),
          CoinCount(Coin(50), count = 4),
          CoinCount(Coin(20), count = 4),
          CoinCount(Coin(10), count = 4),
          CoinCount(Coin(5), count = 4),
          CoinCount(Coin(2), count = 4),
          CoinCount(Coin(1), count = 4),
        ))
      }

      "should be able reset all the current state" in {
        stateRepoForReset.getInsertedCoins should be (coins)
        stateRepoForReset.getSelectedProduct should be (Some(1))
        stateRepoForReset.clear()
        stateRepoForReset.getInsertedCoins should be (Nil)
        stateRepoForReset.getSelectedProduct should be (None)
      }
    }
  }
}
