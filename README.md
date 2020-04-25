# vending-machine
vending-machine
 - Core change calculation algorithm is in /src/main/scala/io/fele/vending_machine/ChangeCalculation.scala
 - Models are defined in /src/main/scala/io/fele/vending_machine/model
 - Data Store layer contains multiple Repository class which are implemented using memory. They can be replaced by a Database implementation 
   /src/main/scala/io/fele/vending_machine/repo
 - VendingStateRepo.scala contains all the business logic
 - Interaction with the vending machine can be found in src/test/scala/io/fele/vending_machine/VendingMachineServiceTest.scala 
 