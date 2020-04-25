package io.fele.vending_machine.repo

import io.fele.vending_machine.model.{Coin, CoinCount}
import org.scalatest.BeforeAndAfter
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class CoinsRepoTest extends AnyFreeSpec with Matchers {
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

  val zeroCoinsRepo = new InMemoryCoinsRepo()
  val coinsRepo = new InMemoryCoinsRepo()
  val coinsForRemoveRepo = new InMemoryCoinsRepo()
  val doubleCoinsRepo = new InMemoryCoinsRepo()
  coinsRepo.loadCoins(coins)
  coinsForRemoveRepo.loadCoins(coins)
  doubleCoinsRepo.loadCoins(coins)
  doubleCoinsRepo.loadCoins(coins)

  "CoinsRepo" - {
    "when contains 0 coins" - {
      zeroCoinsRepo.getCoins should be (Nil)

      "should be able to load 0 coins" in {
        val zeroCoins = Nil
        zeroCoinsRepo.loadCoins(zeroCoins)
        zeroCoinsRepo.getCoins should be (Nil)
      }

      "should be able to load coins" in {
        zeroCoinsRepo.loadCoins(coins)
        zeroCoinsRepo.getCoins should be (coins)
      }

    }

    "when contains many coins" - {
      coinsRepo.getCoins should be (coins)

      "should be able to load 0 coins" in {
        val zeroCoins = Nil
        coinsRepo.loadCoins(zeroCoins)
        coinsRepo.getCoins should be (coins)
      }

      "should be able to load coins" in {
        coinsRepo.loadCoins(coins)
        coinsRepo.getCoins should be (List(
          CoinCount(Coin(200), count = 200),
          CoinCount(Coin(100), count = 200),
          CoinCount(Coin(50), count = 200),
          CoinCount(Coin(20), count = 200),
          CoinCount(Coin(10), count = 200),
          CoinCount(Coin(5), count = 200),
          CoinCount(Coin(2), count = 200),
          CoinCount(Coin(1), count = 200),
        ))
      }

      "should be able to remove coins" in {
        doubleCoinsRepo.removeCoins(coins) should be (Right(()))
        doubleCoinsRepo.getCoins should be (List(
          CoinCount(Coin(200), count = 100),
          CoinCount(Coin(100), count = 100),
          CoinCount(Coin(50), count = 100),
          CoinCount(Coin(20), count = 100),
          CoinCount(Coin(10), count = 100),
          CoinCount(Coin(5), count = 100),
          CoinCount(Coin(2), count = 100),
          CoinCount(Coin(1), count = 100),
        ))
      }

      "given if not enough coin to be removed" - {
        "should not able to remove coins" in {
          val removeCoins = List(
            CoinCount(Coin(200), count = 1),
            CoinCount(Coin(100), count = 1),
            CoinCount(Coin(50), count = 100),
            CoinCount(Coin(20), count = 1000),
            CoinCount(Coin(10), count = 1),
            CoinCount(Coin(5), count = 1),
            CoinCount(Coin(2), count = 1),
            CoinCount(Coin(1), count = 1),
          )
          coinsForRemoveRepo.removeCoins(removeCoins) should be (Left(NotEnoughCoinsException))
          coinsForRemoveRepo.getCoins should be (List(
            CoinCount(Coin(200), count = 100),
            CoinCount(Coin(100), count = 100),
            CoinCount(Coin(50), count = 100),
            CoinCount(Coin(20), count = 100),
            CoinCount(Coin(10), count = 100),
            CoinCount(Coin(5), count = 100),
            CoinCount(Coin(2), count = 100),
            CoinCount(Coin(1), count = 100),
          ))
        }
      }
    }
  }
}
