package com.example.chessbot;

import java.util.HashMap;
import chesspresso.Chess;

public final class PieceSquareTable {
    public static final Integer[] blackPawn = new Integer[]{
         0,  0,  0,  0,  0,  0,  0,  0,
        50, 50, 50, 50, 50, 50, 50, 50,
        10, 10, 20, 30, 30, 20, 10, 10,
         5,  5, 10, 25, 25, 10,  5,  5,
         0,  0,  0, 20, 20,  0,  0,  0,
         5, -5,-10,  0,  0,-10, -5,  5,
         5, 10, 10,-20,-20, 10, 10,  5,
         0,  0,  0,  0,  0,  0,  0,  0
    };
    public static final Integer[] whitePawn = getMirror(blackPawn);
    public static final Integer[] blackKnight = new Integer[]{
        -50,-40,-30,-30,-30,-30,-40,-50,
        -40,-20,  0,  0,  0,  0,-20,-40,
        -30,  0, 10, 15, 15, 10,  0,-30,
        -30,  5, 15, 20, 20, 15,  5,-30,
        -30,  0, 15, 20, 20, 15,  0,-30,
        -30,  5, 10, 15, 15, 10,  5,-30,
        -40,-20,  0,  5,  5,  0,-20,-40,
        -50,-40,-30,-30,-30,-30,-40,-50
    };
    public static final Integer[] whiteKnight = getMirror(blackKnight);
    public static final Integer[] blackBishop = new Integer[]{
        -20,-10,-10,-10,-10,-10,-10,-20,
        -10,  0,  0,  0,  0,  0,  0,-10,
        -10,  0,  5, 10, 10,  5,  0,-10,
        -10,  5,  5, 10, 10,  5,  5,-10,
        -10,  0, 10, 10, 10, 10,  0,-10,
        -10, 10, 10, 10, 10, 10, 10,-10,
        -10,  5,  0,  0,  0,  0,  5,-10,
        -20,-10,-10,-10,-10,-10,-10,-20
    };
    public static final Integer[] whiteBishop = getMirror(blackBishop);
    public static final Integer[] blackRook = new Integer[]{
        0,  0,  0,  0,  0,  0,  0,  0,
        5, 10, 10, 10, 10, 10, 10,  5,
       -5,  0,  0,  0,  0,  0,  0, -5,
       -5,  0,  0,  0,  0,  0,  0, -5,
       -5,  0,  0,  0,  0,  0,  0, -5,
       -5,  0,  0,  0,  0,  0,  0, -5,
       -5,  0,  0,  0,  0,  0,  0, -5,
        0,  0,  0,  5,  5,  0,  0,  0
    };
    public static final Integer[] whiteRook = getMirror(blackRook);
    public static final Integer[] blackQueen = new Integer[]{
        -20,-10,-10, -5, -5,-10,-10,-20,
        -10,  0,  0,  0,  0,  0,  0,-10,
        -10,  0,  5,  5,  5,  5,  0,-10,
         -5,  0,  5,  5,  5,  5,  0, -5,
          0,  0,  5,  5,  5,  5,  0, -5,
        -10,  5,  5,  5,  5,  5,  0,-10,
        -10,  0,  5,  0,  0,  0,  0,-10,
        -20,-10,-10, -5, -5,-10,-10,-20
    };
    public static final Integer[] whiteQueen = getMirror(blackQueen);
    public static final Integer[] blackKing = new Integer[]{
        -30,-40,-40,-50,-50,-40,-40,-30,
        -30,-40,-40,-50,-50,-40,-40,-30,
        -30,-40,-40,-50,-50,-40,-40,-30,
        -30,-40,-40,-50,-50,-40,-40,-30,
        -20,-30,-30,-40,-40,-30,-30,-20,
        -10,-20,-20,-20,-20,-20,-20,-10,
         20, 20,  0,  0,  0,  0, 20, 20,
         20, 30, 10,  0,  0, 10, 30, 20
    };
    public static final Integer[] whiteKing = getMirror(blackKing);

    public static final HashMap<Integer,Integer[]> white = new HashMap<>();
    public static final HashMap<Integer,Integer[]> black = new HashMap<>();

    static {
        white.put((int)Chess.PAWN, whitePawn);
        white.put((int)Chess.KNIGHT, whiteKnight);
        white.put((int)Chess.BISHOP, whiteBishop);
        white.put((int)Chess.ROOK, whiteRook);
        white.put((int)Chess.QUEEN, whiteQueen);
        white.put((int)Chess.KING, whiteKing);

        black.put((int)Chess.PAWN, blackPawn);
        black.put((int)Chess.KNIGHT, blackKnight);
        black.put((int)Chess.BISHOP, blackBishop);
        black.put((int)Chess.ROOK, blackRook);
        black.put((int)Chess.QUEEN, blackQueen);
        black.put((int)Chess.KING, blackKing);
    }

    private static Integer[] getMirror(Integer[] mask){
        final int size = 8;
        Integer[] result = new Integer[mask.length];
        for (int row = 0; row < size; row ++){
            for (int col = 0; col < size; col++){
                result[row * size + col] = mask[(size - 1 - row) * size + col];
            }
        }
        return result;
    }
}