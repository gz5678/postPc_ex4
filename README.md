"I pledge the highest level of ethical principles in support of academic excellence.  I ensure that all of my work reflects my own abilities and not those of someone else."

Question
=========

Testing the CalculateRootsService for good input is pretty easy - we pass in a number and we expect a broadcast intent with the roots.
Testing for big prime numbers can be frustrating - currently the service is hard-coded to run for 20 seconds before giving up, which would make the tests run for too long.

What would you change in the code in order to let the service run for maximum 200ms in tests environments, but continue to run for 20sec max in the real app (production environment)?

Answer
======

There are a few options in order to support this kind of testing:
1. Have the CalculateRootsService receive the timeout duration as a parameter. This way, the service
   can be configured by the process that's running it.
2. Have an if statement which check if the we're running in a test environment or at production.
   The following code from stackoverflow is supposed to help:
   application.getClassLoader().loadClass("foo.bar.test.SomeTest");
3. Create a method inside CalculateRootsService which returns the timeout duration. Then use mockito
   to change output of the methd: when(myB.func2(anyInt())).thenReturn(20);