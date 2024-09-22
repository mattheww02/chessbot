package com.example.chessbot;

import java.util.Random;

import chesspresso.move.IllegalMoveException;
import chesspresso.position.Position;

public class MinimaxBot implements ChessPlayer {

    private int maxDepth;
    private boolean isWhite;

    public MinimaxBot(int maxDepth, boolean isWhite) {
        this.maxDepth = maxDepth;
        this.isWhite = isWhite;
    }

    public short getMove(Position position){
        // pick the move that gives the best material advantage
        int eval = 0;
        short[] legalMoves = position.getAllMoves();
        shuffleArray(legalMoves);
        short bestMove = legalMoves[0];
        for (short move : legalMoves){
            try {
                position.doMove(move);
            } catch (IllegalMoveException e) {}
            int m = position.getMaterial();
            System.out.println(m);
            if ((isWhite && m > eval) || (!isWhite && m < eval)){
                eval = m;
                bestMove = move;
            }
            eval = Math.max(eval, position.getMaterial());
            position.undoMove();
        }
        return bestMove;
    }

    public int evalPosition(Position position, int depth){
        if (depth == 0) return isWhite ? position.getMaterial() : -position.getMaterial();
        short[] moves = position.getAllMoves();
        shuffleArray(moves);
        int bestEval = Integer.MIN_VALUE;
        for (short move : moves){
            try {
                position.doMove(move);
            } catch (IllegalMoveException e) {}
            int eval = evalPosition(position, depth - 1);
            if ((isWhite && eval > bestEval) || (!isWhite && eval < bestEval)){
                bestEval = eval;
            }
            position.undoMove();
        }
        return 0;
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
