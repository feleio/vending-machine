package io.fele.vending_machine.repo

import io.fele.vending_machine.model.{Coin, CoinCount}
import org.scalatest.BeforeAndAfter
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class CoinsRepoTest extends AnyFreeSpec with Matchers with BeforeAndAfter {

  var zeroCoinsRepo: CoinsRepo = _
  var coinsRepo: CoinsRepo = _

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

  before {
    zeroCoinsRepo = new InMemoryCoinsRepo
    coinsRepo = new InMemoryCoinsRepo
    coinsRepo.loadCoins(coins)
  }

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
        coinsRepo.getCoins should be (Nil)
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

    }
  }
}
