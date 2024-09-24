package com.example.chessbot;

import java.util.Arrays;
import java.util.Random;

import chesspresso.Chess;
import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.position.Position;

public class AlphaBetaBot implements ChessPlayer {

    private static final int INFINITY = 50000;
    private final int maxDepth;

    public AlphaBetaBot(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public short getMove(Position position){
        // follow minimax algorithm to get the best move with a lookahead of maxDepth
        short[] legalMoves = getOrderedMoves(position);
        short bestMove = legalMoves[0];
        int bestEval = Integer.MAX_VALUE;
        for (short move : legalMoves){
            try {
                position.doMove(move);
            } catch (IllegalMoveException e) {}
            int eval = maxEval(position, maxDepth - 1, -INFINITY, INFINITY);
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

    public int minEval(Position position, int depth, int alpha, int beta){
        if (position.isStaleMate()) return 0;
        if (position.isMate()) return INFINITY + 1000 * depth;
        if (depth == 0) return -position.getMaterial();

        int minEval = INFINITY;
        short[] moves = getOrderedMoves(position);

        if (moves.length == 0) return 0; //TODO: do something else?

        for (short move : moves) {
            try {
                position.doMove(move);
            } catch (IllegalMoveException e) {} // never reached
            int eval = maxEval(position, depth - 1, alpha, beta) 
                     - positionEval(position, move); //TODO: check + or -
            position.undoMove();

            minEval = Math.min(minEval, eval);
            beta = Math.min(beta, eval);

            if (beta <= alpha) break;
        }

        return minEval;
    }

    public int maxEval(Position position, int depth, int alpha, int beta){
        if (position.isStaleMate()) return 0;
        if (position.isMate()) return -INFINITY - 1000 * depth;
        if (depth == 0) return position.getMaterial();

        int maxEval = -INFINITY;
        short[] moves = getOrderedMoves(position);

        if (moves.length == 0) return 0; //TODO: do something else?

        for (short move : moves) {
            try {
                position.doMove(move);
            } catch (IllegalMoveException e) {} // never reached
            int eval = minEval(position, depth - 1, alpha, beta) 
                     - positionEval(position, move); //TODO: check + or -
            position.undoMove();

            maxEval = Math.max(maxEval, eval);
            alpha = Math.max(alpha, eval);

            if (beta <= alpha) break;
        }

        return maxEval;
    }

    public int positionEval(Position position, short move){
        return positionEval(
            position.getPiece(Move.getToSqi(move)),
            move,
            position.getToPlay() == Chess.WHITE
        );
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

    public short[] getOrderedMoves(Position position){
        // returns legal moves in an optimised order
        short[] cMoves = position.getAllCapturingMoves();
        short[] ncMoves = position.getAllNonCapturingMoves();
        short[] moves = Arrays.copyOf(cMoves, cMoves.length + ncMoves.length);
        System.arraycopy(ncMoves, 0, moves, cMoves.length, ncMoves.length);
        return moves;
    }
}
