package com.example.chessbot;

import java.util.ArrayList;
import java.util.List;

public class ChessBoard {

    public static final byte
        WHITE  = 0, BLACK = 1,
        PAWN   = 0, WHITE_PAWN   = (2 * PAWN + WHITE),   BLACK_PAWN   = (2 * PAWN + BLACK),
        KNIGHT = 1, WHITE_KNIGHT = (2 * KNIGHT + WHITE), BLACK_KNIGHT = (2 * KNIGHT + BLACK),
        BISHOP = 2, WHITE_BISHOP = (2 * BISHOP + WHITE), BLACK_BISHOP = (2 * BISHOP + BLACK),
        ROOK   = 3, WHITE_ROOK   = (2 * ROOK + WHITE),   BLACK_ROOK   = (2 * ROOK + BLACK),
        QUEEN  = 4, WHITE_QUEEN  = (2 * QUEEN + WHITE),  BLACK_QUEEN  = (2 * QUEEN + BLACK),
        KING   = 5, WHITE_KING   = (2 *KING + WHITE),    BLACK_KING   = (2 *KING + BLACK),
        EMPTY  = BLACK_KING + 1;
    
    public static byte getPieceColor(byte pc) { return (byte)(pc & 1); }
    public static byte getPieceType(byte pc) { return (byte)(pc >> 1); }

    public static byte getMovePiece(int move) { return (byte)(move >> 16); }
    public static byte getMoveRow(int move) { return (byte)((move >> 8) & 0xFF); }
    public static byte getMoveCol(int move) { return (byte)(move & 0xFF); }

    public static long getBitMask(byte row, byte col) { return (1 << (row * 8 + col)); }

    public static long[] initialBitBoards = { // WARNING: should be treated as unsigned longs
        0x00FF000000000000L, 0x000000000000FF00L, // pawns
        0x4200000000000000L, 0x0000000000000042L, // knights
        0x2400000000000000L, 0x0000000000000024L, // bishops
        0x8100000000000000L, 0x0000000000000081L, // rooks
        0x0800000000000000L, 0x0000000000000010L, // queens
        0x1000000000000000L, 0x0000000000000008L  // kings
    };

    private long[] bitboards;
    
    private long getColorBitboard(byte color){
        if (color == WHITE)
            return bitboards[WHITE_PAWN]   | bitboards[WHITE_KNIGHT] 
                 | bitboards[WHITE_BISHOP] | bitboards[WHITE_ROOK]
                 | bitboards[WHITE_QUEEN]  | bitboards[WHITE_KING];
        else if (color == BLACK)
            return bitboards[BLACK_PAWN]   | bitboards[BLACK_KNIGHT]
                 | bitboards[BLACK_BISHOP] | bitboards[BLACK_ROOK]
                 | bitboards[BLACK_QUEEN]  | bitboards[BLACK_KING];
        return 0;
    }

    public ChessBoard(){
        bitboards = initialBitBoards;
    }

    public short createMove(byte pc, byte rowFrom, byte colFrom, byte rowTo, byte colTo){
        return (short)(pc << 4 | rowFrom << 3 | colFrom << 2 | rowTo << 1 | colTo);
    }

    public List<Short> getValidMoves(byte pc, byte row, byte col){
        List<Short> moves = new ArrayList<Short>();
        long wbb = getColorBitboard(WHITE); // white bitboard
        long bbb = getColorBitboard(BLACK); // black bitboard
        switch (pc){
            case WHITE_PAWN: //TODO: add pawn promotion, en passant
                if ((wbb & bbb & getBitMask((byte)(row + 1), col)) == 0)
                    moves.add(createMove(pc, row, col, (byte)(row + 1), col));
                if ((col < 7) && ((bbb & getBitMask((byte)(row + 1), (byte)(col + 1))) != 0))
                    moves.add(createMove(pc, row, col, (byte)(row + 1), col));
                if ((col > 0) && ((bbb & getBitMask((byte)(row + 1), (byte)(col - 1))) != 0))
                    moves.add(createMove(pc, row, col, (byte)(row + 1), col));
                break;

            case BLACK_PAWN:
                if ((wbb & bbb & getBitMask((byte)(row - 1), col)) == 0)
                    moves.add(createMove(pc, row, col, (byte)(row - 1), col));
                if ((col < 7) && ((wbb & getBitMask((byte)(row - 1), (byte)(col + 1))) != 0))
                    moves.add(createMove(pc, row, col, (byte)(row - 1), col));
                if ((col > 0) && ((wbb & getBitMask((byte)(row - 1), (byte)(col - 1))) != 0))
                    moves.add(createMove(pc, row, col, (byte)(row - 1), col));
                break;

            case WHITE_KNIGHT: //TODO
                if ((wbb & getBitMask((byte)(row +1), (byte)(col + 2))) == 0)

                break;
        }
        return moves;
    }

    
}
