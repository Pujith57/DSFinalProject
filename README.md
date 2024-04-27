# CompSt 751 Group Project

## Name
Variational DS

## People

Please list all the team members with name, github username and email,e.g. 
1) Pujith Kotha, @kothapujith, kothap@uwm.edu
2) Saketha Gaddam, @SakethaGaddam, saketha@uwm.edu
3) Aniruddh Venkatesh Attignal, @Aniruddh-V-Attignal ,attignal@uwm.edu
4) Sharvari Prabhakanth Ashwini, Sharvari2501, sharvari@uwm.edu


## Paper

Give the URL to the ACM digital library for the paper you will be replicating.
https://dl.acm.org/doi/abs/10.1145/3023956.3023966

### Scope

Describe what you will be implementing.
We are implementing a variational stack as described in the paper that involves creating a Java framework that supports the handling of variability in stack operations. The primary focus will be on implementing the two core concepts: Choice-of-Stacks and Stack-of-Choices, along with the mentioned optimizations like Buffered Stack and Hybrid Stack. 

### Artifacts found

Give URLs of code provided by the authors of the paper or their collaborators.
https://dl.acm.org/doi/10.1145/2465106.2465121
https://dl.acm.org/doi/10.1145/2518190
https://web.engr.oregonstate.edu/~erwig/papers/ChoiceCalculus_TOSEM11.pdf
https://web.engr.oregonstate.edu/~erwig/papers/VPwithCC_GTTSE12
https://link.springer.com/article/10.1007/s00165-019-00479-y

## Plan

### Main classes

What classes will you implement to demonstrate the technique of the paper?
To implement this, we take four classes for different stacks and write methods for operations like push, pop, peek, isEmpty.

### Helper classes

Are there any helpers you need to implement?
These can be from other open source projects, as long as you credit them
SizeofStack();
isEmpty();

### Test Suites

Provide a section for each test suite that is planned and a paragraph describing what it will do.
There will be 2 sections: Primary and Efficiency tests.
Primary Test:
We intend to test the basic functionalities like push, pop, peek, isempty etc for all the 4 different stacks and also check whether the stack is functioning as required when these operations are performed in a row in different combinations
Include tests for edge cases, such as extremely large values, null inputs (where applicable), and complex conditional structures.
Efficiency Test: We intend to add efficiency tests to check both time and space complexities of the 4 stacks. Some of these tests include performing the basic operations on stack multiple times  example like 100000 times an check its performance is as expected. We can also compare the performance of different stacks for same operations. We can do the same for space complexity and compare different stacks.
Data structure well-formed-ness tests: Data structure well-formedness tests are designed to verify the integrity and correctness of a data structure's implementation. Some examples are like verifying if a stack is correctly initialized, if items are correctly added and removed from the stack etc. Verify that the stack remains in a valid state after a series of operations. Well-formedness tests are crucial for ensuring that a data structure is robust, reliable, and performs as expected. 


## Status

Add section using the current date as the heading and describe the status in a few sentences or a few paragraphs.
02/21/2024: We have explored about the project and came up with 4 stacks, we have also included 2 helper classes which we intend to use in upcoming days. We also created a project in Git classroom.
04/14/2024: We have implemented 3 types of stacks - choice of stacks, stack of choices and buffered stack. Added  primary, efficiency and invariant test cases for all three types of stacks.




