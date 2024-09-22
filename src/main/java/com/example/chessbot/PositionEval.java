package com.example.chessbot;

import java.util.HashMap;
import chesspresso.Chess;

public class PositionEval {
    public static final Integer[] whitePawnMask = new Integer[]{
        0,  0,  0,  0,  0,  0,  0,  0,
        0,  0,  0,  0,  0,  0,  0,  0,
        5,  5,  5,  5,  5,  5,  5,  5,
        10, 10, 10, 10, 10, 10, 10, 10,
        15, 15, 15, 15, 15, 15, 15, 15,
        20, 20, 20, 20, 20, 20, 20, 20,
        25, 25, 25, 25, 25, 25, 25, 25,
        0,  0,  0,  0,  0,  0,  0,  0
    };
    public static final Integer[] blackPawnMask = getReverse(whitePawnMask);
    public static final Integer[] whiteKnightMask = new Integer[]{
        0,  0,  0,  0,  0,  0,  0,  0,
        0,  0,  0,  0,  0,  0,  0,  0,
        0,  0,  15, 15, 15, 15, 0,  0,
        0,  0,  15, 30, 30, 15, 0,  0,
        0,  0,  15, 30, 30, 15, 0,  0,
        0,  0,  15, 15, 15, 15, 0,  0,
        0,  0,  0,  0,  0,  0,  0,  0,
        0,  0,  0,  0,  0,  0,  0,  0
    };
    public static final Integer[] blackKnightMask = whiteKnightMask;

    public static final HashMap<Integer,Integer[]> whitePieceMasks = new HashMap<>();
    public static final HashMap<Integer,Integer[]> blackPieceMasks = new HashMap<>();

    static {
        whitePieceMasks.put((int)Chess.PAWN, whitePawnMask);
        whitePieceMasks.put((int)Chess.KNIGHT, whiteKnightMask);

        blackPieceMasks.put((int)Chess.PAWN, blackPawnMask);
        blackPieceMasks.put((int)Chess.KNIGHT, blackKnightMask);
    }

    private static Integer[] getReverse(Integer[] mask){
        Integer[] reverseMask = new Integer[mask.length];
        for (int i = 0; i < mask.length; i++)
            reverseMask[i] = mask[mask.length - i - 1];
        return reverseMask;
    }
}