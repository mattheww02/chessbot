import java.util.*;

public class ChessGame {
    public static final int BOARD_SIZE = 8;
    public static final char EMPTY = '.';

    // white pieces upper case, black pieces lower case
    public static final char[] INITIAL_BOARD = {
        'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r',
        'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p',
        '.', '.', '.', '.', '.', '.', '.', '.',
        '.', '.', '.', '.', '.', '.', '.', '.',
        '.', '.', '.', '.', '.', '.', '.', '.',
        '.', '.', '.', '.', '.', '.', '.', '.',
        'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P',
        'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'
    };

    public ChessGame(){

    }

    public boolean isLegalMove(Move move){
        return false;
    }

    public char getPieceAt(int square){
        return ' ';
    }

    public static void main(String[] args) {
        ChessBot ai = new ChessBot();
        char[] board = ChessGame.INITIAL_BOARD.clone();
        boolean isWhiteTurn = true;

        // Basic game loop
        while (!ai.isGameOver(board)) {
            if (isWhiteTurn) {
                System.out.println("White to move.");
                // Human player move (to be implemented)
            } else {
                System.out.println("AI thinking...");
                Move aiMove = ai.getBestMove(board, 3, false); // AI move at depth 3
                board = ai.makeMove(board, aiMove);
            }

            // Toggle turn
            isWhiteTurn = !isWhiteTurn;
        }

        System.out.println("Game over!");
    }
}
