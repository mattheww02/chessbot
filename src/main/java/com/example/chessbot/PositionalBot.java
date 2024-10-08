package com.example.chessbot;

import java.util.Random;

import chesspresso.Chess;
import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.position.Position;

public class PositionalBot implements ChessPlayer {

    private final int maxDepth;

    public PositionalBot(int maxDepth) {
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
            int eval = minMaxEval(position, maxDepth - 1, false);
            eval -= positionEval(position.getPiece(Move.getToSqi(move)), move, position.getColor(Move.getToSqi(move)) == Chess.WHITE);
            //System.out.println(eval);
            if (eval < bestEval){
                bestEval = eval;
                bestMove = move;
            }
            position.undoMove();
        }
        //System.out.println("-------------------------\n" + bestEval + "\n-------------------------");
        return bestMove;
    }

    public int minMaxEval(Position position, int depth, boolean isMin){
        if (position.isStaleMate()) return 0;
        if (position.isMate()) return isMin ? 50000 : -50000;
        if (depth == 0) return isMin ? -position.getMaterial() : position.getMaterial();
        short[] moves = position.getAllMoves();
        shuffleArray(moves);
        int bestEval = isMin ? 50000 : -50000;
        for (short move : moves){
            try{
                position.doMove(move);
            }
            catch (IllegalMoveException e) {}
            int eval = minMaxEval(position, depth - 1, !isMin) - positionEval(position.getPiece(Move.getToSqi(move)), move, position.getToPlay() == Chess.WHITE);
            if ((isMin && eval < bestEval) || (!isMin && eval > bestEval)){
                bestEval = eval;
            }
            position.undoMove();
        }
        return bestEval;
    }

    public int positionEval(int piece, short move, boolean isWhite){
        if (isWhite && PieceSquareTable.white.containsKey(piece)){
            return PieceSquareTable.white.get(piece)[Move.getToSqi(move)] - PieceSquareTable.white.get(piece)[Move.getFromSqi(move)];
        }
        else if (PieceSquareTable.black.containsKey(piece)){
            return PieceSquareTable.black.get(piece)[Move.getToSqi(move)] - PieceSquareTable.black.get(piece)[Move.getFromSqi(move)];
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
