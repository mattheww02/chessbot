package com.example.chessbot;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import chesspresso.move.Move;
import chesspresso.Chess;

public class InteractiveBoardGUI extends ChessBoardGUI {
    private final UserPlayer userPlayer; // The player that will interact with the GUI
    private int sourceRow = -1, sourceCol = -1; // Track the user's selected piece

    public InteractiveBoardGUI(UserPlayer userPlayer) {
        super(false);
        this.userPlayer = userPlayer;
        initFrame();
    }

    private void initFrame() {
        // instantiate board squares
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JPanel square = new JPanel();
                square.setBackground((row + col) % 2 == 0 ? ChessBoardGUI.LIGHT_COLOR : ChessBoardGUI.DARK_COLOR);
                
                // add mouse listener to capture clicks
                final int r = row, c = col;
                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        handleSquareClick(r, c);
                    }
                });
                
                frame.add(square);
            }
        }

        frame.setSize(600, 600);
        frame.setVisible(true);
    }

    private void handleSquareClick(int row, int col) {
        if (sourceRow == -1 && sourceCol == -1){
            // first click selects a piece
            sourceRow = row;
            sourceCol = col;
        } 
        else{
            // second click selects the target square
            int targetRow = row;
            int targetCol = col;
            
            // get source and destination in Chesspresso format
            int source = Chess.coorToSqi(sourceCol, sourceRow);
            int dest = Chess.coorToSqi(targetCol, targetRow);
            
            // set the move for the player
            userPlayer.setMove(source, dest);

            // reset source square
            sourceRow = -1;
            sourceCol = -1;
        }
    }
}
