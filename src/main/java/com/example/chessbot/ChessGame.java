package com.example.chessbot;

import chesspresso.position.Position;
import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.game.Game;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ChessGame {

    private Position position;
    private ChessBoardGUI gui;
    private Timer timer;

    public ChessGame() {
        position = Position.createInitialPosition(); // initialise game
        gui = new ChessBoardGUI(); // initialize board gui
        gui.updateBoard(position);
        startGameLoop();
    }

    private void startGameLoop() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!position.isTerminal()) {
                    makeRandomMove();
                } else {
                    timer.cancel();
                }
            }
        }, 0, 500); // delay (milliseconds)
    }

    private void makeRandomMove() {
        short[] legalMoves = position.getAllMoves(); // Get legal moves (?)
        if (legalMoves.length > 0) {
            int randomIndex = new Random().nextInt(legalMoves.length);
            short move = legalMoves[randomIndex];
            makeMove(move);
            gui.updateBoard(position);
        }
    }

    public Position getPosition() {
        return position;
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
        ChessGame game = new ChessGame();

    }
}