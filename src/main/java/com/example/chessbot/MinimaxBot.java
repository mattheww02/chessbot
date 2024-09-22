package com.example.chessbot;

import java.util.Random;

import chesspresso.move.IllegalMoveException;
import chesspresso.position.Position;

public class MinimaxBot implements ChessPlayer {

    private final int maxDepth;

    public MinimaxBot(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public short getMove(Position position){
        // follow minimax algorithm to get the best move with a lookahead of maxDepth
        short[] legalMoves = position.getAllMoves();
        shuffleArray(legalMoves);
        short bestMove = legalMoves[0];
        int bestEval = Integer.MAX_VALUE;
        for (short move : legalMoves){
            try {
                position.doMove(move);
            } catch (IllegalMoveException e) {}
            int eval = evalPosition(position, maxDepth - 1, false);
            if (eval < bestEval){
                bestEval = eval;
                bestMove = move;
            }
            position.undoMove();
        }
        return bestMove;
    }

    public int evalPosition(Position position, int depth, boolean isMin){
        if (depth == 0) return position.getMaterial();
        short[] moves = position.getAllMoves();
        shuffleArray(moves);
        int bestEval = isMin ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        for (short move : moves){
            try{
                position.doMove(move);
            }
            catch (IllegalMoveException e) {}
            int eval = evalPosition(position, depth - 1, !isMin);
            if ((isMin && eval < bestEval) || (!isMin && eval > bestEval)){
                bestEval = eval;
            }
            position.undoMove();
        }
        return bestEval;
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
