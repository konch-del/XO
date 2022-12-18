import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class TicTacToe {
    private TTTBoard board = new TTTBoard();
    private Scanner scanner = new Scanner(System.in);
    private Agent agent = new Agent(30, 0.2);
    private Agent agent2 = new Agent(30, 0.7);
    private List<Double> agentX = new ArrayList<>();
    private List<Double> agentO = new ArrayList<>();
    private List<Double> agent2X = new ArrayList<>();
    private List<Double> agent2O = new ArrayList<>();
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
            if(agent.getPiece().equals(TTTPiece.X)){
                agentX.add(agent.getMaxReward().get("X"));
                agent2O.add(agent2.getMaxReward().get("O"));
            }else{
                agentO.add(agent.getMaxReward().get("O"));
                agent2X.add(agent2.getMaxReward().get("X"));
            }
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

    public void printPlot() throws IOException {
        XYPlot plotX = new XYPlot();
        XYPlot plotO = new XYPlot();
        DefaultXYDataset datasetX = new DefaultXYDataset();
        DefaultXYDataset datasetO = new DefaultXYDataset();
        double[][] seriesX = new double[2][agentX.size()];
        for(int i = 0; i < agentX.size(); i++){
            seriesX[0][i] = i;
            seriesX[1][i] = agentX.get(i);
        }
        double[][] seriesX2 = new double[2][agent2X.size()];
        for(int i = 0; i < agent2X.size(); i++){
            seriesX2[0][i] = i;
            seriesX2[1][i] = agent2X.get(i);
        }
        datasetX.addSeries("agent1X", seriesX);
        datasetX.addSeries("agent2X", seriesX2);
        plotX.setDataset(datasetX);
        File file = new File("D:\\java_new\\XO\\plotX.png");
        ChartUtils.saveChartAsPNG(file, ChartFactory
                .createXYLineChart("", "Reward", "Iterations",
                        datasetX,
                        PlotOrientation.VERTICAL,
                        true, true, true), 600, 600);
        double[][] seriesO = new double[2][agentO.size()];
        for(int i = 0; i < agentO.size(); i++){
            seriesO[0][i] = i;
            seriesO[1][i] = agentO.get(i);
        }
        double[][] seriesO2 = new double[2][agent2O.size()];
        for(int i = 0; i < agent2O.size(); i++){
            seriesO2[0][i] = i;
            seriesO2[1][i] = agent2O.get(i);
        }
        datasetO.addSeries("agent1O", seriesO);
        datasetO.addSeries("agent2O", seriesO2);
        plotO.setDataset(datasetO);
        File file2 = new File("D:\\java_new\\XO\\plotO.png");
        ChartUtils.saveChartAsPNG(file2, ChartFactory
                .createXYLineChart("", "Reward", "Iterations",
                        datasetO,
                        PlotOrientation.VERTICAL,
                        true, true, true), 600, 600);
    }

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        TicTacToe toe = new TicTacToe();
        toe.training(100);
        System.out.println("Agent");
        toe.agent.printStats();
        System.out.println("Agent2");
        toe.agent2.printStats();
        toe.saveAgent("Agent.ser", toe.getAgent());
        toe.saveAgent("Agent2.ser", toe.getAgent2());
        //toe.runGameWithAgent(toe.getAgent());
        toe.printPlot();
    }
}
