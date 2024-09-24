package com.example.chessbot;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import chesspresso.position.Position;

public class ChessBoardGUI {
    protected static final int SIZE = 8;
    protected static final Color LIGHT_COLOR = new Color(255, 206, 158);
    protected static final Color DARK_COLOR = new Color(165, 105, 48);

    private final JFrame frame;
    protected final JPanel boardPanel;
    private final JLabel[] messages = new JLabel[3];

    private float whiteWins = 0, blackWins = 0;

    private final Map<Integer, ImageIcon> whitePieceImages = new HashMap<>();
    private final Map<Integer, ImageIcon> blackPieceImages = new HashMap<>();

    public ChessBoardGUI() { this(true); }

    protected ChessBoardGUI(boolean defaultInit) {
        // initialise piece icons
        loadPieceImages();

        // initialise frame
        frame = new JFrame("Chess Board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // initialise board and squares
        boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        boardPanel.setPreferredSize(new Dimension(600, 600));
        if (defaultInit) initSquares();

        // initialise toolbar messages
        JPanel messagePanel = initMessages();

        // add components to frame and make visible
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(messagePanel, BorderLayout.SOUTH);
        frame.setSize(600, 675);
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

    private void initSquares() {
        // instantiate board squares
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JPanel square = new JPanel();
                square.setBackground((row + col) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
                boardPanel.add(square);
            }
        }
    }

    private JPanel initMessages() {
        // initialise toolbar messages
        messages[0] = new JLabel("        White: 0", SwingConstants.LEFT);
        messages[0].setFont(new Font("Arial", Font.PLAIN, 18));
        messages[1] = new JLabel("", SwingConstants.CENTER);
        messages[1].setFont(new Font("Arial", Font.BOLD, 30));
        messages[1].setForeground(Color.RED);
        messages[2] = new JLabel("Black: 0        ", SwingConstants.RIGHT);
        messages[2].setFont(new Font("Arial", Font.PLAIN, 18));

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(messages[0], BorderLayout.WEST);
        messagePanel.add(messages[1], BorderLayout.CENTER);
        messagePanel.add(messages[2], BorderLayout.EAST);
        messagePanel.setPreferredSize(new Dimension(600, 75));

        return messagePanel;
    }

    public void updateBoard(Position position) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JPanel square = (JPanel) boardPanel.getComponent(row * SIZE + col);
                square.removeAll(); // clear previous piece

                int vRow = SIZE - 1 - row;

                int piece = position.getPiece(vRow * SIZE + col); // get piece on given square
                if (piece != chesspresso.Chess.NO_PIECE) {
                    int color = position.getColor(vRow * SIZE + col);
                    JLabel label = new JLabel(color == chesspresso.Chess.WHITE ? whitePieceImages.get(piece) : blackPieceImages.get(piece));
                    square.add(label); // add piece image to square
                }

                square.revalidate();
                square.repaint();
            }
        }
    }

    public void endOfGame(String message, float w, float b) {
        whiteWins += w;
        blackWins += b;
        messages[1].setText(message);
        messages[0].setText("        White: " + whiteWins);
        messages[2].setText(" Black: " + blackWins + "        ");
    }
}
