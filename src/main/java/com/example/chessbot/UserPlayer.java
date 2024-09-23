package com.example.chessbot;

import chesspresso.position.Position;
import chesspresso.move.Move;
import chesspresso.Chess;
import chesspresso.move.IllegalMoveException;

public class UserPlayer implements ChessPlayer {

    private volatile short move; // user-chosen move
    private boolean moveReady = false;
    private Position position;

    @Override
    public short getMove(Position position) {
        moveReady = false; // reset move state
        this.position = position;

        // wait for the user to select a valid move via GUI interaction
        synchronized (this) {
            while (!moveReady) {
                try {
                    wait(); // wait until the move is ready
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return move;
    }

    public void setMove(int source, int dest) throws IllegalMoveException {
        short[] legalMoves = position.getAllMoves();
        
        for (short legalMove : legalMoves){
            if (Move.getFromSqi(legalMove) == source && Move.getToSqi(legalMove) == dest){
                move = legalMove;
                moveReady = true;
                // notify waiting thread that move is ready to be played
                synchronized (this) {
                    notifyAll();
                }
                break;
            }
        }

        throw new IllegalMoveException("Move " + source + " to " + dest + " is illegal.");
    }
}
