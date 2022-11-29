import java.util.Scanner;
public class TicTacToe {
    private TTTBoard board = new TTTBoard();
    private Scanner scanner = new Scanner(System.in);
    private Integer getPlayerMove() {
        Integer playerMove = -1;
        while (!board.getLegalMoves().contains(playerMove)) {
            System.out.println("Enter a legal square (1-9):");
            Integer play = scanner.nextInt() - 1;
            playerMove = play;
        }
        return playerMove;
    }
    private void runGame() {
        Agent agent = new Agent(30);
        agent.setPiece(TTTPiece.O);
        // главный цикл игры
        while (true) {
            Integer humanMove = getPlayerMove();
            board = board.move(humanMove);
            if (board.isWin()) {
                System.out.println("Human wins!");
                agent.recalculate(board);
                break;
            } else if (board.isDraw()) {
                System.out.println("Draw!");
                break;
            }
            //Integer computerMove = Minimax.findBestMove(board, 9);
            //System.out.println("Computer move is " + computerMove);
            //board = board.move(computerMove);
            board = board.move(agent.move(board));
            System.out.println(board);
            if (board.isWin()) {
                System.out.println("Computer wins!");
                agent.recalculate(board);
                break;
            } else if (board.isDraw()) {
                System.out.println("Draw!");
                break;
            }
        }
    }
    public static void main(String[] args) {
        new TicTacToe().runGame();
    }
}
