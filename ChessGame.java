import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

class InvalidMoveException extends Exception {
    public InvalidMoveException(String message) {
        super(message);
    }
}

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

    public static final Map<Character,String> charIcons = new HashMap<Character,String>() {{
        put('P',"♙"); put('N',"♘"); put('B',"♗"); put('R',"♖"); put('Q',"♕"); put('K',"♔");
        put('p',"♟"); put('n',"♞"); put('b',"♝"); put('r',"♜"); put('q',"♛"); put('k',"♚");
    }};

    public char[] board;

    public ChessGame(){
        board = INITIAL_BOARD.clone();
    }

    private boolean isDiffColor(char a, char b){
        return (a == EMPTY || b == EMPTY || ((Character.toLowerCase(a) == a) != (Character.toLowerCase(b) == b)));
    }

    public boolean isLegalMove(Move move){
        if (move.from == move.to || board[move.from] == EMPTY ||
            isDiffColor(board[move.from], board[move.to])) return false;

        return true;
    }

    public List<Move> getLegalMoves(boolean isWhite){
        List<Move> moves = new ArrayList<Move>();
        if (isWhite){
            
        }
        return new ArrayList<>();
    }

    public boolean isGameOver() {
        // Simple example: check if one of the kings is missing
        boolean hasWhiteKing = false, hasBlackKing = false;
        for (char piece : board) {
            if (piece == 'K') hasWhiteKing = true;
            if (piece == 'k') hasBlackKing = true;
        }
        return !hasWhiteKing || !hasBlackKing;
    }

    public void printBoard() {
        for (int i = 0; i < board.length; i += BOARD_SIZE){
            for (int j = 0; j < BOARD_SIZE; j++){
                //System.out.println(charIcons.get(board[i + j]));
                System.out.print(board[i + j]);
            }
            System.out.println();
        }
    }

    private void updateBoard(Move move) throws InvalidMoveException {
        System.out.println("Updating board...");
        if (!isLegalMove(move)) throw new InvalidMoveException(move.toString());
        System.out.println(move.to + ": " + board[move.to]);
        System.out.println(move.from + ": " + board[move.from]);
        board[move.to] = board[move.from];
        board[move.from] = EMPTY;
    }

    public static void main(String[] args) {
        Player whitePlayer = new RandomBot();
        Player blackPlayer = new RandomBot();
        ChessGame game = new ChessGame();
        boolean isWhiteTurn = true;
        game.printBoard();

        while (!game.isGameOver()) {
            int from = (int)(Math.random() * game.board.length);
            int to = (int)(Math.random() * game.board.length);
            Move move = new Move(from, to);
            if (game.isLegalMove(move))
                try {
                    game.updateBoard(move);
                } catch (InvalidMoveException e) {
                    System.out.println("Invalid move!");
                }
            game.printBoard();
        }
        System.out.println("Game over!");
    }
}
