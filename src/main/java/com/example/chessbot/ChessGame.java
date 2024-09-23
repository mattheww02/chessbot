package com.example.chessbot;

import chesspresso.position.Position;
import chesspresso.move.IllegalMoveException;
import java.util.Timer;
import java.util.TimerTask;

public class ChessGame {

    private final Position position;
    private final ChessBoardGUI gui;
    private final ChessPlayer white;
    private final ChessPlayer black;
    private Timer timer;

    public ChessGame(ChessPlayer white, ChessPlayer black) {
        
        this.white = white;
        this.black = black;
        position = Position.createInitialPosition(); // initialise game
        if (white instanceof UserPlayer)
            gui = new InteractiveBoardGUI((UserPlayer) white);
        else if (black instanceof UserPlayer)
            gui = new InteractiveBoardGUI((UserPlayer) black);
        else
            gui = new ChessBoardGUI(); // initialize board gui
        gui.updateBoard(position);
        startGameLoop();
    }

    private void startGameLoop() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (position.isTerminal()) 
                    timer.cancel();
                else if (position.getToPlay() == chesspresso.Chess.WHITE)
                    makeMove(white.getMove(new Position(position)));
                else
                    makeMove(black.getMove(new Position(position)));
            }
        }, 0, 500); // delay (milliseconds)
    }

    public void makeMove(short move) {
        try{
            position.doMove(move);
        }
        catch (IllegalMoveException e){
            System.out.println("Illegal move: " + e.getMessage());
            return;
        }
        gui.updateBoard(position);
    }

    public static void main(String[] args) {
        ChessGame game = new ChessGame(
            new MinimaxBot(3),
            new AlphaBetaBot(5)
        );
    }
}