package com.example.chessbot;

import javax.swing.*;
import java.awt.*;
import chesspresso.*;
import chesspresso.position.Position;

public class ChessBoardGUI {
    private static final int SIZE = 8; // Size of the chessboard
    private static final Color LIGHT_COLOR = new Color(255, 206, 158);
    private static final Color DARK_COLOR = new Color(165, 105, 48);

    private JFrame frame; // Declare the JFrame
    private JPanel[][] squares; // Array to hold the square panels

    public ChessBoardGUI() {
        frame = new JFrame("Chess Board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(SIZE, SIZE));
        squares = new JPanel[SIZE][SIZE]; // Initialize the squares array

        // Create the board
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                squares[row][col] = new JPanel();
                squares[row][col].setBackground((row + col) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
                JLabel label = new JLabel("", SwingConstants.CENTER);
                label.setFont(new Font("Arial Unicode MS", Font.PLAIN, 40));
                squares[row][col].add(label);
                frame.add(squares[row][col]);
            }
        }

        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    private String pieceToString(int piece, int color) {
        if (color == chesspresso.Chess.WHITE){
            switch (piece) {
                case chesspresso.Chess.KNIGHT: return "♘";
                case chesspresso.Chess.BISHOP: return "♗";
                case chesspresso.Chess.ROOK: return "♖";
                case chesspresso.Chess.QUEEN: return "♕";
                case chesspresso.Chess.KING: return "♔";
                case chesspresso.Chess.PAWN: return "♙";
                default: return ""; // Empty square
            }
        }
        switch (piece) {
            case chesspresso.Chess.KNIGHT: return "♞";
            case chesspresso.Chess.BISHOP: return "♝";
            case chesspresso.Chess.ROOK: return "♜";
            case chesspresso.Chess.QUEEN: return "♛";
            case chesspresso.Chess.KING: return "♚";
            case chesspresso.Chess.PAWN: return "♟";
            default: return ""; // Empty square
        }
    }

    public void updateBoard(Position position) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                int piece = position.getPiece(row * 8 + col); // Get piece at specific square
                int color = position.getColor(row * 8 + col);
                JLabel label;

                if (piece == 0) {
                    label = new JLabel();
                } else {
                    label = new JLabel(pieceToString(piece, color), SwingConstants.CENTER);
                }

                label.setFont(new Font("Arial Unicode MS", Font.PLAIN, 40));
                squares[row][col].removeAll(); // Clear previous piece
                squares[row][col].add(label);
                squares[row][col].revalidate(); // Refresh the square
                squares[row][col].repaint();
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessBoardGUI());
    }
}
