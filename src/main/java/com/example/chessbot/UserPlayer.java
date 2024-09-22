package com.example.chessbot;

import chesspresso.position.Position;
import chesspresso.move.Move;
import chesspresso.Chess;
import chesspresso.move.IllegalMoveException;

public class UserPlayer implements ChessPlayer {

    private volatile short move; // user-chosen newMove
    private boolean moveReady = false;
    private Position position;

    @Override
    public short getMove(Position position) {
        moveReady = false; // reset newMove state
        this.position = position;

        // wait for the user to select a valid newMove via GUI interaction
        synchronized (this) {
            while (!moveReady) {
                try {
                    wait(); // wait until the newMove is ready
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return move;
    }

    public void setMove(int source, int dest) {
        boolean captures = position.getColor(dest) != Chess.NOBODY;
        short newMove = Move.getRegularMove(source, dest, captures);

        try {
            position.doMove(newMove);
        } catch (IllegalMoveException ex) {
            return;
        }

        this.move = newMove;
        moveReady = true;
        // notify the waiting thread that the newMove is ready
        synchronized (this) {
            notifyAll();
        }
    }
}
