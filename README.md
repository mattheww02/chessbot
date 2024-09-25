# Java-Based Chess Engine

This project features a chess-playing AI which uses a minimax algorithm combined with piece square tables, a transposition table and several other techniques to calculate the strongest chess moves.

In testing against other bots, the engine was able to play to an estimated rating of 2000 elo (98th percentile of chess players).

There is also a custom-built GUI and the ability to play against the bot, either as a human or with another chess engine.

To build and run the project:
- ensure [Maven](https://maven.apache.org/download.cgi) is installed (I used version 3.9)
- navigate to the root directory
- to build: `mvn clean install`
- to run: `mvn exec:java -Dexec.mainClass="com.example.chessbot.ChessGame"`

The bots and players, their colours, and the number of rounds can be changed in `main()` of the main class, `ChessGame`. You can also create your own bot by implementing the `ChessPlayer` interface.
