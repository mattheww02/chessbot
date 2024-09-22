package com.example.chessbot;

import java.util.Random;

import chesspresso.move.IllegalMoveException;
import chesspresso.position.Position;

public class MaterialBot implements ChessPlayer {
    public short getMove(Position position){
        // pick the move that gives the best material advantage
        int material = Integer.MAX_VALUE;
        short[] legalMoves = position.getAllMoves();
        shuffleArray(legalMoves);
        short bestMove = legalMoves[0]; 
        for (short move : legalMoves){
            try {
                position.doMove(move);
            } catch (IllegalMoveException e) {}
            int m = position.getMaterial();
            if (m < material){
                material = m;
                bestMove = move;
            }
            position.undoMove();
        }
        return bestMove;
    }

    static void shuffleArray(short[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--){
            int index = rnd.nextInt(i + 1);
            // swap at indices
            short a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
