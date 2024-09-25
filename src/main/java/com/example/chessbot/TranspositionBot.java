package com.example.chessbot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import chesspresso.Chess;
import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.position.Position;

public class TranspositionBot implements ChessPlayer {

    private static final int INFINITY = 50000;
    private static final int CASTLE_BONUS = 50;
    private final Map<Long,Integer> positionHistory;
    private final Cache<Long,Integer> transpositionTable;
    private final int initialMaxDepth, finalMaxDepth;
    private int maxDepth;
    private float gameStage;

    public TranspositionBot(int initialMaxDepth) {
        this(initialMaxDepth, initialMaxDepth * 2, 10000);
    }

    public TranspositionBot(int initialMaxDepth, int finalMaxDepth) {
        this(initialMaxDepth, finalMaxDepth, 10000);
    }

    public TranspositionBot(int initialMaxDepth, int finalMaxDepth, int cacheSize) {
        this.initialMaxDepth = initialMaxDepth;
        this.finalMaxDepth = finalMaxDepth;
        this.positionHistory = new HashMap<>();
        this.transpositionTable = new Cache<>(cacheSize);
    }

    public short getMove(Position position){
        // update this object with relevant information
        transpositionTable.clear();
        updatePositionHistory(position);
        gameStage = getGameStage(position);
        maxDepth = initialMaxDepth + (int)((finalMaxDepth - initialMaxDepth) * gameStage);

        // follow minimax algorithm to get the best move with a lookahead of maxDepth
        short[] legalMoves = getOrderedMoves(position);
        short bestMove = legalMoves[0];
        int bestEval = Integer.MAX_VALUE;
        for (short move : legalMoves){
            tryMove(position, move);

            long hash = position.getHashCode();
            int eval;
            eval = maxEval(position, maxDepth - 1, -INFINITY, INFINITY) 
                 - positionEval(position, move);
            transpositionTable.put(hash, eval);

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
        short[] moves = depth > 0 ? getOrderedMoves(position) : position.getAllCapturingMoves();

        if (moves.length == 0) return -position.getMaterial();

        for (short move : moves) {
            tryMove(position, move);

            long hash = position.getHashCode();
            int eval;

            eval = maxEval(position, depth - 1, alpha, beta) 
                - positionEval(position, move);
            transpositionTable.put(hash, eval);
            
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

            long hash = position.getHashCode();
            int eval;

            eval = minEval(position, depth - 1, alpha, beta) 
                - positionEval(position, move);
            transpositionTable.put(hash, eval); //TODO: check that this gives the correct eval
             
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
        else{
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

    private float getGameStage(Position position) {
        final int[] pieceVals = {0, 1, 1, 3, 10, 0, 0};
        int whiteMaterial = 0, blackMaterial = 0;
        for (int sqi = 0; sqi < 64; sqi++){
            final int piece = position.getPiece(sqi);
            final int color = position.getColor(sqi);
            if (color == Chess.WHITE) whiteMaterial += pieceVals[piece];
            else blackMaterial += pieceVals[piece];
        }
        if (Math.min(whiteMaterial, blackMaterial) > 10) return 0; // opening/mid game
        return 1 - (float)Math.min(whiteMaterial, blackMaterial) / 10; // increases to 1 in end game
    }
}
