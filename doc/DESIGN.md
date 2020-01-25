#Lorne Zhang: frontend, backend, and all other functionalities

##The project's design goals include:
1. Making different classes for different objects in the game.
2. Making different methods to handle different parts of the game.

##New features to add:

I wanted to make it easy to add new bricks and new power-ups to the game.

##High level design and interactions between core classes:

The high level design: generate a stage and splash screen, update screens based on level, refresh objects inside screens.

The core classes: BrickHeavy and PowerUp. These two classes are added into the scenes and are updated in the step function.

##What assumptions or decisions were made to simplify your project's design, especially those that affected adding required features

In order to simplify the project's design, I made generic functions like generateBlocks2, Step, and setUpLevel that are able to update and initiate all levels.

##Describe, in detail, how to add new features to your project, especially ones you were not able to complete by the deadline

I have realized that my design is not the best for features because the majority of the elements are made in main. I did not think of making different classes for different aspects of the level. I 