package com.example.chessbot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import chesspresso.Chess;
import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.position.Position;

public class AlphaBetaBotV2 implements ChessPlayer {

    private static final int INFINITY = 50000;
    private final int maxDepth;
    private final Map<Long,Integer> positionHistory;

    public AlphaBetaBotV2(int maxDepth) {
        this.maxDepth = maxDepth;
        this.positionHistory = new HashMap<>();
    }

    public short getMove(Position position){
        // follow minimax algorithm to get the best move with a lookahead of maxDepth
        short[] legalMoves = getOrderedMoves(position);
        short bestMove = legalMoves[0];
        int bestEval = Integer.MAX_VALUE;
        for (short move : legalMoves){
            try {
                position.doMove(move);
                updatePositionHistory(position);
            } catch (IllegalMoveException e) {}
            int eval = maxEval(position, maxDepth - 1, -INFINITY, INFINITY);
            eval -= positionEval(position.getPiece(Move.getToSqi(move)), move, position.getColor(Move.getToSqi(move)) == Chess.WHITE);
            //System.out.println(eval);
            if (eval < bestEval){
                bestEval = eval;
                bestMove = move;
            }
            undoPositionHistory(position);
            position.undoMove();
        }
        //System.out.println("-------------------------\n" + bestEval + "\n-------------------------");
        try {
            position.doMove(bestMove);
            updatePositionHistory(position);
        } catch (IllegalMoveException e) {}        
        return bestMove;
    }

    private int minEval(Position position, int depth, int alpha, int beta){
        if (position.isStaleMate()) return 0; //TODO: stalemate should eval to zero regardless of position
        if (position.isMate()) return INFINITY + 1000 * depth;
        if (position.isTerminal()) return 0; //TODO: avoid draw by repetition in winning positions
        if (positionHistory.getOrDefault(position.getHashCode(), 0) >= 3) return -INFINITY;
        if (depth == 0) return -position.getMaterial();

        int minEval = INFINITY;
        short[] moves = getOrderedMoves(position);

        if (moves.length == 0) return 0; //TODO: do something else?

        for (short move : moves) {
            try {
                position.doMove(move);
                updatePositionHistory(position);
            } catch (IllegalMoveException e) {} // never reached
            int eval = maxEval(position, depth - 1, alpha, beta) 
                     - positionEval(position, move); //TODO: check + or -
            undoPositionHistory(position);
            position.undoMove();

            minEval = Math.min(minEval, eval);
            beta = Math.min(beta, eval);

            if (beta <= alpha) break;
        }

        return minEval;
    }

    private int maxEval(Position position, int depth, int alpha, int beta){
        if (position.isStaleMate()) return 0; //TODO: stalemate should eval to zero regardless of position
        if (position.isMate()) return -INFINITY - 1000 * depth;
        if (position.isTerminal()) return 0; //TODO: avoid draw by repetition in winning positions
        if (positionHistory.getOrDefault(position.getHashCode(), 0) >= 3) return INFINITY;
        if (depth == 0) return position.getMaterial();

        int maxEval = -INFINITY;
        short[] moves = getOrderedMoves(position);

        if (moves.length == 0) return 0; //TODO: do something else?

        for (short move : moves) {
            try {
                position.doMove(move);
                updatePositionHistory(position);
            } catch (IllegalMoveException e) {} // never reached
            int eval = minEval(position, depth - 1, alpha, beta) 
                     - positionEval(position, move); //TODO: check + or -
            undoPositionHistory(position);
            position.undoMove();

            maxEval = Math.max(maxEval, eval);
            alpha = Math.max(alpha, eval);

            if (beta <= alpha) break;
        }

        return maxEval;
    }

    private void updatePositionHistory(Position position){
        long hash = position.getHashCode();
        positionHistory.put(hash, positionHistory.getOrDefault(hash, 0) + 1);
    }

    private void undoPositionHistory(Position position){
        long hash = position.getHashCode();
        if (positionHistory.containsKey(hash)){
            if (positionHistory.get(hash) > 1)
                positionHistory.put(hash, positionHistory.get(hash) + 1);
            else
               positionHistory.remove(hash);
        }
    }

    private int positionEval(Position position, short move){
        return positionEval(
            position.getPiece(Move.getToSqi(move)),
            move,
            position.getToPlay() == Chess.WHITE
        );
    }

    private int positionEval(int piece, short move, boolean isWhite){
        if (isWhite && PieceSquareTable.white.containsKey(piece)){
            return PieceSquareTable.white.get(piece)[Move.getToSqi(move)] - PieceSquareTable.white.get(piece)[Move.getFromSqi(move)];
        }
        else if (PieceSquareTable.black.containsKey(piece)){
            return PieceSquareTable.black.get(piece)[Move.getToSqi(move)] - PieceSquareTable.black.get(piece)[Move.getFromSqi(move)];
        }
        return 0;
    }

    private short[] getOrderedMoves(Position position){
        // returns legal moves in an optimised order
        short[] cMoves = position.getAllCapturingMoves();
        short[] ncMoves = position.getAllNonCapturingMoves();
        short[] moves = Arrays.copyOf(cMoves, cMoves.length + ncMoves.length);
        System.arraycopy(ncMoves, 0, moves, cMoves.length, ncMoves.length);
        return moves;
    }
}
