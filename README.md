# vending-machine
vending-machine
 - Core change calculation algorithm is in [ChangeCalculation.scala](https://github.com/feleio/vending-machine/blob/master/src/main/scala/io/fele/vending_machine/ChangeCalculation.scala)
 - Models are defined in [model](https://github.com/feleio/vending-machine/tree/master/src/main/scala/io/fele/vending_machine/model)
 - Data Store layer contains [multiple Repository classes](https://github.com/feleio/vending-machine/tree/master/src/main/scala/io/fele/vending_machine/repo) which are implemented using memory. They can be replaced by a Database implementation 
 - [VendingStateRepo.scala](https://github.com/feleio/vending-machine/blob/master/src/main/scala/io/fele/vending_machine/VendingMachineService.scala) contains all the business logic
 - Interaction with the vending machine can be found in [VendingMachineServiceTest.scala](https://github.com/feleio/vending-machine/blob/master/src/test/scala/io/fele/vending_machine/VendingMachineServiceTest.scala) 
 
# Running test
After installing java jdk, scala and sbt, run:
```shell script
sbt test
```
