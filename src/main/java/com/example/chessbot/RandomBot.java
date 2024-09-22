package com.example.chessbot;

import java.util.Random;

import chesspresso.position.Position;

public class RandomBot implements ChessPlayer {
    public short getMove(Position position){
        // generate all legal moves and pick one at random
        short[] legalMoves = position.getAllMoves();
        int idx = new Random().nextInt(legalMoves.length);
        short move = legalMoves[idx];
        return move;
    }
}
