package com.example.chessbot;

import chesspresso.position.Position;
import chesspresso.Chess;
import chesspresso.move.IllegalMoveException;
import java.util.Timer;
import java.util.TimerTask;

public class ChessGame {

    private final ChessBoardGUI gui;
    private final ChessPlayer white;
    private final ChessPlayer black;
    private Position position;
    private Timer timer;
    private int numGames;

    public ChessGame(ChessPlayer white, ChessPlayer black) { this(white, black, 1); }

    public ChessGame(ChessPlayer white, ChessPlayer black, int numGames) {
        this.white = white;
        this.black = black;
        this.numGames = numGames;
        
        // initialise board GUI for 0 or 1 user-controlled players
        if (white instanceof UserPlayer)
            gui = new InteractiveBoardGUI((UserPlayer) white);
        else if (black instanceof UserPlayer)
            gui = new InteractiveBoardGUI((UserPlayer) black);
        else
            gui = new ChessBoardGUI();

        startGameLoop();
    }

    private void startGameLoop() {
        if (numGames > 0) numGames--;
        position = Position.createInitialPosition(); // set up the pieces
        gui.updateBoard(position);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (position.isTerminal()) {
                    timer.cancel();
                    handleGameEnd();
                }
                else if (position.getToPlay() == chesspresso.Chess.WHITE)
                    makeMove(white.getMove(new Position(position)));
                else
                    makeMove(black.getMove(new Position(position)));
            }
        }, 500, 500); // delay (milliseconds)
    }

    private void makeMove(short move) {
        try{
            position.doMove(move);
        }
        catch (IllegalMoveException e){
            System.out.println("Illegal move: " + e.getMessage());
            return;
        }
        gui.updateBoard(new Position(position));
    }

    private void handleGameEnd() {
        if (position.isStaleMate()) {
            gui.endOfGame("STALEMATE", 0.5f, 0.5f);
        }
        else if (position.isMate()) {
            if (position.getToPlay() == Chess.WHITE)
                gui.endOfGame("BLACK WINS", 0, 1);
            else
                gui.endOfGame("WHITE WINS", 1, 0);
        }
        else {
            gui.endOfGame("DRAW", 0.5f, 0.5f);
        }

        if (numGames != 0){
            // start next game after a delay
            Timer endOfGameTimer = new Timer();
            endOfGameTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    gui.endOfGame("", 0, 0);
                    startGameLoop();
                }
            }, 3000);
        }
    }

    public static void main(String[] args) {
        ChessGame game = new ChessGame(
            new AlphaBetaBotV2(5),
            new AlphaBetaBotV2(5),
            10
        );
    } 
}
// to build:  mvn clean install
// to run:    mvn exec:java -Dexec.mainClass="com.example.chessbot.ChessGame"
