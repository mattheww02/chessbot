package com.example.chessbot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import chesspresso.Chess;
import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.position.Position;

public class AlphaBetaBotV3 implements ChessPlayer {

    private static final int INFINITY = 50000;
    private static final int CASTLE_BONUS = 50;
    private final int maxDepth;
    private final Map<Long,Integer> positionHistory;
    private final Cache<Long,Integer> transpositionTable;

    public AlphaBetaBotV3(int maxDepth) {
        this.maxDepth = maxDepth;
        this.positionHistory = new HashMap<>();
        this.transpositionTable = new Cache<>(10000);
    }

    public short getMove(Position position){
        // follow minimax algorithm to get the best move with a lookahead of maxDepth
        updatePositionHistory(position);
        short[] legalMoves = getOrderedMoves(position);
        short bestMove = legalMoves[0];
        int bestEval = Integer.MAX_VALUE;
        for (short move : legalMoves){
            tryMove(position, move);
            int eval = maxEval(position, maxDepth - 1, -INFINITY, INFINITY) 
                     - positionEval(position, move);
            if (eval < bestEval){
                bestEval = eval;
                bestMove = move;
            }
            untryMove(position);
        }
        tryMove(position, bestMove); // try best move again to add it to the position history     
        return bestMove;
    }

    private int minEval(Position position, int depth, int alpha, int beta){
        // eval for minimising player
        if (position.isStaleMate()) return 0;
        if (position.isMate()) return INFINITY + 1000 * depth;
        if (position.isTerminal()) return 0;
        if (positionHistory.getOrDefault(position.getHashCode(), 0) >= 3) return -INFINITY;
        //if (depth == 0) return -position.getMaterial();

        int minEval = INFINITY;
        short[] moves = depth > 0 ? getOrderedMoves(position) : position.getAllCapturingMoves(); //TODO: do this faster?

        if (moves.length == 0) return -position.getMaterial();

        for (short move : moves) {
            tryMove(position, move);
            int eval = maxEval(position, depth - 1, alpha, beta) 
                     - positionEval(position, move);
            untryMove(position);

            minEval = Math.min(minEval, eval);
            beta = Math.min(beta, eval);

            if (beta <= alpha) break;
        }

        return minEval;
    }

    private int maxEval(Position position, int depth, int alpha, int beta){
        // eval for maximising player
        if (position.isStaleMate()) return 0; //TODO: stalemate should eval to zero regardless of position
        if (position.isMate()) return -INFINITY - 1000 * depth;
        if (position.isTerminal()) return 0; //TODO: avoid draw by repetition in winning positions
        if (positionHistory.getOrDefault(position.getHashCode(), 0) >= 3) return INFINITY;
        //if (depth == 0) return position.getMaterial();

        int maxEval = -INFINITY;
        short[] moves = depth > 0 ? getOrderedMoves(position) : position.getAllCapturingMoves();

        if (moves.length == 0) return position.getMaterial();

        for (short move : moves) {
            tryMove(position, move);
            int eval = minEval(position, depth - 1, alpha, beta) 
                     - positionEval(position, move);
            untryMove(position);

            maxEval = Math.max(maxEval, eval);
            alpha = Math.max(alpha, eval);

            if (beta <= alpha) break;
        }

        return maxEval;
    }

    private void tryMove(Position position, short move){
        // try a legal move
        try {
            position.doMove(move);
            updatePositionHistory(position);
        } catch (IllegalMoveException e) {
            System.out.println("Illegal move: " + e.getMessage());
        }
    }

    private void untryMove(Position position){
        // undo a move that was tried
        undoPositionHistory(position);
        position.undoMove();
    }

    private void updatePositionHistory(Position position){
        // add current position to position history
        long hash = position.getHashCode();
        positionHistory.put(hash, positionHistory.getOrDefault(hash, 0) + 1);
    }

    private void undoPositionHistory(Position position){
        // remove current position from position history
        long hash = position.getHashCode();
        if (positionHistory.containsKey(hash)){
            if (positionHistory.get(hash) > 1)
                positionHistory.put(hash, positionHistory.get(hash) + 1);
            else
               positionHistory.remove(hash);
        }
    }

    private int positionEval(Position position, short move){
        // evaluate position based on most recent move
        int piece = position.getPiece(Move.getToSqi(move));
        boolean isWhite = position.getToPlay() != Chess.WHITE;
        int result = 0;

        // check if castling is still possible
        // if ((position.getCastles() & (isWhite ? Position.WHITE_CASTLE : Position.BLACK_CASTLE)) != 0){
        //     result -= CASTLE_BONUS;
        // } //TODO: fix

        if (Move.isCastle(move)){
            result += CASTLE_BONUS;
            // deal with positional changes due to castling
            if (isWhite){
                if (Move.isShortCastle(move)){
                    result += PieceSquareTable.white.get((int)Chess.KING)[6];
                    result += PieceSquareTable.white.get((int)Chess.ROOK)[5]
                            - PieceSquareTable.white.get((int)Chess.ROOK)[7];
                }
                else{
                    result += PieceSquareTable.white.get((int)Chess.KING)[2];
                    result += PieceSquareTable.white.get((int)Chess.ROOK)[3]
                            - PieceSquareTable.white.get((int)Chess.ROOK)[0];
                }
                result -= PieceSquareTable.white.get((int)Chess.KING)[4];
            }
            else{
                if (Move.isShortCastle(move)){
                    result += PieceSquareTable.black.get((int)Chess.KING)[62];
                    result += PieceSquareTable.black.get((int)Chess.ROOK)[61]
                            - PieceSquareTable.black.get((int)Chess.ROOK)[63];
                }
                else{
                    result += PieceSquareTable.black.get((int)Chess.KING)[2];
                    result += PieceSquareTable.black.get((int)Chess.ROOK)[3]
                            - PieceSquareTable.black.get((int)Chess.ROOK)[0];
                }
                result -= PieceSquareTable.black.get((int)Chess.KING)[60];
            }
        }
        else{ //TODO: check if this is actually getting the right PST values
            // lookup piece square table to evaluate new position after move
            if (isWhite && PieceSquareTable.white.containsKey(piece)){
                result += PieceSquareTable.white.get(piece)[Move.getToSqi(move)] - PieceSquareTable.white.get(piece)[Move.getFromSqi(move)];
            }
            else if (PieceSquareTable.black.containsKey(piece)){
                result += PieceSquareTable.black.get(piece)[Move.getToSqi(move)] - PieceSquareTable.black.get(piece)[Move.getFromSqi(move)];
            }
        }

        return result;
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
