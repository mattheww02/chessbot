import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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

    public static final Map<Character,String> charIcons = new HashMap<Character,String>() {{
        put('P',"♙"); put('N',"♘"); put('B',"♗"); put('R',"♖"); put('Q',"♕"); put('K',"♔");
        put('p',"♟"); put('n',"♞"); put('b',"♝"); put('r',"♜"); put('q',"♛"); put('k',"♚");
    }};

    public char[] board;

    public ChessGame(){
        board = INITIAL_BOARD.clone();
    }

    public boolean isLegalMove(Move move){
        return false;
    }

    public char getPieceAt(int square){
        return ' ';
    }

    public List<Move> getLegalMoves(){
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

    private void updateBoard(Move move) {

    }

    public static void main(String[] args) {
        Player whitePlayer = new RandomBot();
        Player blackPlayer = new RandomBot();
        ChessGame game = new ChessGame();
        boolean isWhiteTurn = true;
        game.printBoard();

        // while (!game.isGameOver()){
        //     if (isWhiteTurn){
        //         System.out.println("White to move.");
        //         Move whiteMove = whitePlayer.getMove(game.board, true);
        //         game.updateBoard(whiteMove);
        //     }
        //     else{
        //         System.out.println("Black to move.");
        //         Move blackMove = blackPlayer.getMove(game.board, false);
        //         game.updateBoard(blackMove);
        //     }
        //     isWhiteTurn = !isWhiteTurn;
        // }

        System.out.println("Game over!");
    }
}
