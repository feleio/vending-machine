package io.fele.vending_machine

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import io.fele.vending_machine.model.{Coin, CoinCount}

class ChangeCalculationTest extends AnyFreeSpec with Matchers {
  "ChangeCalculation" - {
    "when contains coins for all types" - {
      val availableCoins: List[CoinCount] = List(
        CoinCount(Coin(200), count = 100),
        CoinCount(Coin(100), count = 100),
        CoinCount(Coin(50), count = 100),
        CoinCount(Coin(20), count = 100),
        CoinCount(Coin(10), count = 100),
        CoinCount(Coin(5), count = 100),
        CoinCount(Coin(2), count = 100),
        CoinCount(Coin(1), count = 100),
      )

      "should be able to give correct change" in {
        ChangeCalculation.getChange(availableCoins, changeAmount = 38800) should equal (Some(
          List(
            CoinCount(Coin(200), count = 100),
            CoinCount(Coin(100), count = 100),
            CoinCount(Coin(50), count = 100),
            CoinCount(Coin(20), count = 100),
            CoinCount(Coin(10), count = 100),
            CoinCount(Coin(5), count = 100),
            CoinCount(Coin(2), count = 100),
            CoinCount(Coin(1), count = 100),
          )
        ))

        ChangeCalculation.getChange(availableCoins, changeAmount = 188) should equal (Some(
          List(
            CoinCount(Coin(100), count = 1),
            CoinCount(Coin(50), count = 1),
            CoinCount(Coin(20), count = 1),
            CoinCount(Coin(10), count = 1),
            CoinCount(Coin(5), count = 1),
            CoinCount(Coin(2), count = 1),
            CoinCount(Coin(1), count = 1),
          )
        ))

        ChangeCalculation.getChange(availableCoins, changeAmount = 10) should equal (Some(
          List(
            CoinCount(Coin(10), count = 1),
          )
        ))

        ChangeCalculation.getChange(availableCoins, changeAmount = 11) should equal (Some(
          List(
            CoinCount(Coin(10), count = 1),
            CoinCount(Coin(1), count = 1),
          )
        ))

        ChangeCalculation.getChange(availableCoins, changeAmount = 2001) should equal (Some(
          List(
            CoinCount(Coin(200), count = 10),
            CoinCount(Coin(1), count = 1),
          )
        ))
      }

      "and not enough coin for the change" - {
        "should not be able to give change" in {
          ChangeCalculation.getChange(availableCoins, changeAmount = 38801) should equal (None)
        }
      }
    }


    "when contains coins of some type" - {
      val availableCoins: List[CoinCount] = List(
        CoinCount(Coin(100), count = 100),
        CoinCount(Coin(1), count = 100),
      )

      "and enough coin for the change" - {
        "should be able to give correct change" in {
          ChangeCalculation.getChange(availableCoins, changeAmount = 10) should equal (Some(
            List(
              CoinCount(Coin(1), count = 10),
            )
          ))

          ChangeCalculation.getChange(availableCoins, changeAmount = 11) should equal (Some(
            List(
              CoinCount(Coin(1), count = 11),
            )
          ))

          ChangeCalculation.getChange(availableCoins, changeAmount = 2001) should equal (Some(
            List(
              CoinCount(Coin(100), count = 20),
              CoinCount(Coin(1), count = 1),
            )
          ))

          ChangeCalculation.getChange(availableCoins, changeAmount = 10100) should equal (Some(
            List(
              CoinCount(Coin(100), count = 100),
              CoinCount(Coin(1), count = 100),
            )
          ))
        }
      }

      "and not enough coin for the change" - {
        "should not be able to give change" in {
          ChangeCalculation.getChange(availableCoins, changeAmount = 10101) should equal (None)
        }
      }
    }

    "when contains 0 coins" - {
      val availableCoins: List[CoinCount] = Nil // empty list
      "should be able to give change == 0" in {
        ChangeCalculation.getChange(availableCoins, changeAmount = 0) should equal (Some(Nil))
      }

      "should not be able to give change > 0" in {
        ChangeCalculation.getChange(availableCoins, changeAmount = 1) should equal (None)
        ChangeCalculation.getChange(availableCoins, changeAmount = 100) should equal (None)
      }
    }
  }
}
