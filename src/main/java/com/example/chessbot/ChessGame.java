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

    public ChessGame(ChessPlayer white_, ChessPlayer black_) {
        position = Position.createInitialPosition(); // initialise game
        gui = new ChessBoardGUI(); // initialize board gui
        white = white_;
        black = black_;
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
            //TODO
        }
        gui.updateBoard(position);
    }

    public static void main(String[] args) {
        ChessGame game = new ChessGame(new RandomBot(), new MaterialBot());

    }
}