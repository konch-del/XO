import java.io.*;
import java.util.Scanner;
public class TicTacToe {
    private TTTBoard board = new TTTBoard();
    private Scanner scanner = new Scanner(System.in);
    private Agent agent = new Agent(30);
    private Agent agent2 = new Agent(30);
    private Integer getPlayerMove() {
        Integer playerMove = -1;
        while (!board.getLegalMoves().contains(playerMove)) {
            System.out.println("Enter a legal square (1-9):");
            Integer play = scanner.nextInt() - 1;
            playerMove = play;
        }
        return playerMove;
    }
    private void runGameWithMinimax() {
        while (true) {
            Integer humanMove = getPlayerMove();
            board = board.move(humanMove);
            if (board.isWin()) {
                System.out.println("Human wins!");
                break;
            } else if (board.isDraw()) {
                System.out.println("Draw!");
                break;
            }
            Integer computerMove = Minimax.findBestMove(board, 9);
            System.out.println("Computer move is " + computerMove);
            board = board.move(computerMove);
            System.out.println(board);
            if (board.isWin()) {
                System.out.println("Computer wins!");
                break;
            } else if (board.isDraw()) {
                System.out.println("Draw!");
                break;
            }
        }
    }

    private void training(int count) throws InterruptedException {
        for(int i = 0; i < count; i++){
            if(Math.random() * 2 > 1) {
                agent.setPiece(TTTPiece.X);
                agent2.setPiece(TTTPiece.O);
            }else{
                agent.setPiece(TTTPiece.O);
                agent2.setPiece(TTTPiece.X);
            }
            while(true) {
                Thread.sleep(100);
                if (agent.getPiece().equals(TTTPiece.X)) {
                    board = board.move(agent.move(board));
                    if (board.isWin()) {
                        agent.recalculateX(board);
                        agent2.recalculateO(board);
                        break;
                    } else if (board.isDraw()) {
                        agent.recalculateX(board);
                        agent2.recalculateO(board);
                        break;
                    }
                    board = board.move(agent2.move(board));
                    if (board.isWin()) {
                        agent2.recalculateO(board);
                        agent.recalculateX(board);
                        break;
                    } else if (board.isDraw()) {
                        agent2.recalculateO(board);
                        agent.recalculateX(board);
                        break;
                    }
                } else {
                    board = board.move(agent2.move(board));
                    if (board.isWin()) {
                        agent2.recalculateX(board);
                        agent.recalculateO(board);
                        break;
                    } else if (board.isDraw()) {
                        agent2.recalculateX(board);
                        agent.recalculateO(board);
                        break;
                    }
                    board = board.move(agent.move(board));
                    if (board.isWin()) {
                        agent.recalculateO(board);
                        agent2.recalculateX(board);
                        break;
                    } else if (board.isDraw()) {
                        agent.recalculateO(board);
                        agent2.recalculateX(board);
                        break;
                    }
                }
            }
            board.reset();
        }
    }

    public void runGameWithAgent(Agent agent){
        while (true) {
            Integer humanMove = getPlayerMove();
            board = board.move(humanMove);
            if (board.isWin()) {
                System.out.println("Human wins!");
                break;
            } else if (board.isDraw()) {
                System.out.println("Draw!");
                break;
            }
            board = board.move(agent.move(board));
            System.out.println(board);
            if (board.isWin()) {
                System.out.println("Computer wins!");
                agent.recalculateO(board);
                break;
            } else if (board.isDraw()) {
                System.out.println("Draw!");
                break;
            }
        }
        board.reset();
    }

    public void saveAgent(String file, Agent agent) throws IOException {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(agent);
    }

    public Agent readAgent(String file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        return (Agent) objectInputStream.readObject();
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Agent getAgent2() {
        return agent2;
    }

    public void setAgent2(Agent agent2) {
        this.agent2 = agent2;
    }

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        TicTacToe toe = new TicTacToe();
        toe.training(20000);
        System.out.println("Agent");
        toe.agent.printStats();
        System.out.println("Agent2");
        toe.agent2.printStats();
        toe.saveAgent("Agent.ser", toe.getAgent());
        toe.saveAgent("Agent2.ser", toe.getAgent2());
        toe.runGameWithAgent(toe.getAgent());
    }
}
