package com.example.chessbot;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import chesspresso.position.Position;

public class ChessBoardGUI {
    private static final int SIZE = 8;
    private static final Color LIGHT_COLOR = new Color(255, 206, 158);
    private static final Color DARK_COLOR = new Color(165, 105, 48);

    private final JFrame frame;
    private final Map<Integer, ImageIcon> whitePieceImages = new HashMap<>();
    private final Map<Integer, ImageIcon> blackPieceImages = new HashMap<>();

    public ChessBoardGUI() {
        // initialise piece icons
        loadPieceImages();
        
        // initialise frame and board squares
        frame = new JFrame("Chess Board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(SIZE, SIZE));

        // instantiate board squares
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JPanel square = new JPanel();
                square.setBackground((row + col) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
                frame.add(square);
            }
        }

        frame.setSize(600, 600);
        frame.setVisible(true);
    }

    private void loadPieceImages() {
        whitePieceImages.put((int)chesspresso.Chess.PAWN, new ImageIcon(getClass().getResource("/images/pw.png")));
        whitePieceImages.put((int)chesspresso.Chess.ROOK, new ImageIcon(getClass().getResource("/images/rw.png")));
        whitePieceImages.put((int)chesspresso.Chess.KNIGHT, new ImageIcon(getClass().getResource("/images/nw.png")));
        whitePieceImages.put((int)chesspresso.Chess.BISHOP, new ImageIcon(getClass().getResource("/images/bw.png")));
        whitePieceImages.put((int)chesspresso.Chess.QUEEN, new ImageIcon(getClass().getResource("/images/qw.png")));
        whitePieceImages.put((int)chesspresso.Chess.KING, new ImageIcon(getClass().getResource("/images/kw.png")));

        blackPieceImages.put((int)chesspresso.Chess.PAWN, new ImageIcon(getClass().getResource("/images/pb.png")));
        blackPieceImages.put((int)chesspresso.Chess.ROOK, new ImageIcon(getClass().getResource("/images/rb.png")));
        blackPieceImages.put((int)chesspresso.Chess.KNIGHT, new ImageIcon(getClass().getResource("/images/nb.png")));
        blackPieceImages.put((int)chesspresso.Chess.BISHOP, new ImageIcon(getClass().getResource("/images/bb.png")));
        blackPieceImages.put((int)chesspresso.Chess.QUEEN, new ImageIcon(getClass().getResource("/images/qb.png")));
        blackPieceImages.put((int)chesspresso.Chess.KING, new ImageIcon(getClass().getResource("/images/kb.png")));
    }

    private String pieceToString(int piece, int color) {
        // return unicode icon for the given piece + color
        if (color == chesspresso.Chess.WHITE){
            switch (piece) {
                case chesspresso.Chess.KNIGHT: return "♘";
                case chesspresso.Chess.BISHOP: return "♗";
                case chesspresso.Chess.ROOK: return "♖";
                case chesspresso.Chess.QUEEN: return "♕";
                case chesspresso.Chess.KING: return "♔";
                case chesspresso.Chess.PAWN: return "♙";
                default: return "";
            }
        }
        switch (piece) {
            case chesspresso.Chess.KNIGHT: return "♞";
            case chesspresso.Chess.BISHOP: return "♝";
            case chesspresso.Chess.ROOK: return "♜";
            case chesspresso.Chess.QUEEN: return "♛";
            case chesspresso.Chess.KING: return "♚";
            case chesspresso.Chess.PAWN: return "♟";
            default: return "";
        }
    }

    public void updateBoard(Position position) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JPanel square = (JPanel) frame.getContentPane().getComponent(row * SIZE + col);
                square.removeAll(); // clear previous piece

                int piece = position.getPiece(row * SIZE + col); // get piece on given square
                if (piece != chesspresso.Chess.NO_PIECE) {
                    int color = position.getColor(row * SIZE + col);
                    JLabel label = new JLabel(color == chesspresso.Chess.WHITE ? whitePieceImages.get(piece) : blackPieceImages.get(piece));
                    square.add(label); // add piece image to square
                }

                square.revalidate();
                square.repaint();
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessBoardGUI());
    }
}
