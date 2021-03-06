# Bowling

The task is to create a Java library for calculation a score of the bowling game and displaying a current state of the game. You need to create an API and its implementation. You can find the rules of the game on http://bowling.about.com/od/rulesofthegame/a/bowlingscoring.htm.

## API needs to be able to:

* Add points of the last roll for a player. You need to take into account that the player has one roll in a frame in the case of strike, also the player can have three rolls in the tenth frame.

* There should be a possibility to see the score of each player in any moment of the game.

* There should be a possibility to see the whole score table for any player.

The solution should perform all necessary checks for conformance to the game rules and general game logic.

## Tips for the implementation

* It is not necessary to create a command line or GUI user interface. It is enough to have unit tests coverage, which prove that the API works.

* You don't need to save the state and the result of the game or read them from a file, it is enough to have the score calculation working in memory only.

* The priorities during the evaluation of the assignment will be the code style, good object-oriented design and the code reusability. The code reusability means that the API can be used with different user interfaces (Web, Swing, console etc.) and hence the implementation does not depend on any user interface.
