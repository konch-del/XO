import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Agent implements Serializable {
    private DirectedWeightedMultigraph<Moves, DefaultWeightedEdge> xMoves = new DirectedWeightedMultigraph<Moves, DefaultWeightedEdge>(DefaultWeightedEdge.class);
    private DirectedWeightedMultigraph<Moves, DefaultWeightedEdge> oMoves = new DirectedWeightedMultigraph<Moves, DefaultWeightedEdge>(DefaultWeightedEdge.class);
    private TTTPiece piece;
    private BigInteger moves;
    private int chance;
    private int[] random;
    private int wins = 0;
    private int lose = 0;

    public Agent(int chance){
        this.chance = chance;
        fillRandom();
    }

    private void fillRandom(){
        int[] random = new int[100];
        for(int i = 0; i < 100 - chance; i++){
            random[i] = 1;
        }
        for(int i = 100 - chance; i < 100; i++){
            random[i] = 0;
        }
        this.random = random;
    }

    private int random(){
        int number = (int) (Math.random() * 100);
        while(number == 100){
            number = (int) (Math.random() * 100);
        }
        return this.random[number];
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
        this.random = null;
        fillRandom();
    }

    public TTTPiece getPiece() {
        return piece;
    }

    public void setPiece(TTTPiece piece) {
        this.piece = piece;
    }

    private void saveMovesX(){
        Moves currentMoves = new Moves(moves);
        if(!xMoves.containsVertex(currentMoves)){
            xMoves.addVertex(currentMoves);
            if(xMoves.vertexSet().size() > 1 && !moves.divide(BigInteger.valueOf(10)).equals(BigInteger.valueOf(0))) {
                List<Moves> previousMoves = xMoves.vertexSet().stream().toList();
                try {
                    Moves previousMove = previousMoves.stream().filter(o -> o.getMove().equals(moves.divide(BigInteger.valueOf(10)))).findFirst().get();
                    xMoves.addEdge(previousMove, currentMoves);
                }catch (Exception e){
                    System.out.println(currentMoves.getMove());
                    List<BigInteger> list = previousMoves.stream().map(o -> o.getMove()).collect(Collectors.toList());
                    for(BigInteger o : list){
                        System.out.print(o + " ");
                    }
                    System.out.println(wins + " " + lose);
                }

            }
        }
    }

    private void saveMovesO(){
        Moves currentMoves = new Moves(moves);
        if(!oMoves.containsVertex(currentMoves)){
            oMoves.addVertex(currentMoves);
            if(oMoves.vertexSet().size() > 1 && !moves.divide(BigInteger.valueOf(10)).equals(BigInteger.valueOf(0))) {
                List<Moves> previousMoves = oMoves.vertexSet().stream().toList();
                Moves previousMove = previousMoves.stream().filter(o -> o.getMove().equals(moves.divide(BigInteger.valueOf(10)))).findFirst().get();
                oMoves.addEdge(previousMove, currentMoves);
            }
        }
    }

    private int computeX(TTTBoard board){
        Moves currentMoves = xMoves.vertexSet().stream().filter(o -> o.getMove().equals(moves)).findFirst().get();
        Moves nextMove;
        List<Moves> nextMoves = xMoves.outgoingEdgesOf(currentMoves).stream().map(o -> (xMoves.getEdgeTarget(o))).collect(Collectors.toList());
        List<BigInteger> legalMoves = board.getLegalMoves().stream().map(o -> BigInteger.valueOf(o)).collect(Collectors.toList());
        nextMoves = nextMoves.stream().filter(o -> legalMoves.contains(o.getMove().mod(BigInteger.valueOf(10)).subtract(BigInteger.valueOf(1)))).collect(Collectors.toList());
        if(random() == 1 && !nextMoves.isEmpty()){
            DefaultWeightedEdge edge = nextMoves.stream()
                    .map(o -> xMoves.getEdge(currentMoves, o))
                    .max((o1, o2) -> {if(xMoves.getEdgeWeight(o1) - xMoves.getEdgeWeight(o2) > 0){
                        return 1;
                    }else{
                        if(xMoves.getEdgeWeight(o1) - xMoves.getEdgeWeight(o2) < 0){
                            return -1;
                        }
                    }
                        return 0;
                    }).get();
            nextMove = xMoves.getEdgeTarget(edge);
            return nextMove.getMove().mod(BigInteger.valueOf(10)).intValue();
        }else{
            ArrayList<Moves> newMovies = new ArrayList<>(xMoves.outgoingEdgesOf(currentMoves).stream().map(o -> (xMoves.getEdgeTarget(o))).collect(Collectors.toList()));
            newMovies = (ArrayList<Moves>) newMovies.stream().filter(o -> board.getLegalMoves().contains(o.getMove())).collect(Collectors.toList());
            List<Integer> newMoviess = newMovies.stream()
                    .map(Moves::getMove)
                    .map(o -> o.mod(BigInteger.valueOf(10)))
                    .map(BigInteger::intValue)
                    .collect(Collectors.toList());
            newMoviess.addAll(board.getLegalMoves().stream().map(o -> o = o + 1).collect(Collectors.toList()));
            int move = (int) (Math.random() * newMoviess.size());
            while(move == 0){
                move = (int) (Math.random() * newMoviess.size());
            }
            return newMoviess.get(move);
        }
    }

    private int computeO(TTTBoard board){
        Moves currentMoves = oMoves.vertexSet().stream().filter(o -> o.getMove().equals(moves)).findFirst().get();
        Moves nextMove;
        List<Moves> nextMoves = oMoves.outgoingEdgesOf(currentMoves).stream().map(o -> (oMoves.getEdgeTarget(o))).collect(Collectors.toList());
        List<BigInteger> legalMoves = board.getLegalMoves().stream().map(o -> BigInteger.valueOf(o)).collect(Collectors.toList());
        nextMoves = nextMoves.stream().filter(o -> legalMoves.contains(o.getMove().mod(BigInteger.valueOf(10)).subtract(BigInteger.valueOf(1)))).collect(Collectors.toList());
        if(random() == 1 && !nextMoves.isEmpty()){
            DefaultWeightedEdge edge = nextMoves.stream()
                    .map(o -> oMoves.getEdge(currentMoves, o))
                    .max((o1, o2) -> {if(oMoves.getEdgeWeight(o1) - oMoves.getEdgeWeight(o2) > 0){
                        return 1;
                    }else{
                        if(oMoves.getEdgeWeight(o1) - oMoves.getEdgeWeight(o2) < 0){
                            return -1;
                        }
                    }
                        return 0;
                    }).get();
            nextMove = oMoves.getEdgeTarget(edge);
            return nextMove.getMove().mod(BigInteger.valueOf(10)).intValue();
        }else{
            ArrayList<Moves> newMovies = new ArrayList<>(oMoves.outgoingEdgesOf(currentMoves).stream().map(o -> (oMoves.getEdgeTarget(o))).collect(Collectors.toList()));
            newMovies = (ArrayList<Moves>) newMovies.stream().filter(o -> board.getLegalMoves().contains(o.getMove())).collect(Collectors.toList());
            //newMovies.remove(oMoves.getEdgeTarget(edge));
            List<Integer> newMoviess = newMovies.stream()
                    .map(Moves::getMove)
                    .map(o -> o.mod(BigInteger.valueOf(10)))
                    .map(BigInteger::intValue)
                    .collect(Collectors.toList());
            newMoviess.addAll(board.getLegalMoves().stream().map(o -> o = o + 1).collect(Collectors.toList()));
            int move = (int) (Math.random() * newMoviess.size());
            while(move == 0){
                move = (int) (Math.random() * newMoviess.size());
            }
            return newMoviess.get(move);
        }
    }

    public void recalculateX(TTTBoard board){
        int result = board.result(this.piece);
        List<Moves> Moves = xMoves.vertexSet().stream().toList();
        if(result > 0){
            while(!moves.equals(BigInteger.valueOf(0)) && !moves.divide(BigInteger.valueOf(10)).equals(BigInteger.valueOf(0))){

                Moves previousMove = Moves.stream().filter(o -> o.getMove().equals(moves.divide(BigInteger.valueOf(10)))).findFirst().get();
                Moves currentMove = Moves.stream().filter(o -> o.getMove().equals(moves)).findFirst().get();

                xMoves.setEdgeWeight(previousMove, currentMove, xMoves.getEdgeWeight(xMoves.getEdge(previousMove, currentMove)) + 1.0);
                moves = moves.divide(BigInteger.valueOf(10));

            }
            wins++;
        }else{
            if(result < 0) {
                while (!moves.equals(BigInteger.valueOf(0)) && !moves.divide(BigInteger.valueOf(10)).equals(BigInteger.valueOf(0))) {
                    Moves previousMove = Moves.stream().filter(o -> o.getMove().equals(moves.divide(BigInteger.valueOf(10)))).findFirst().get();
                    Moves currentMove = Moves.stream().filter(o -> o.getMove().equals(moves)).findFirst().get();

                    xMoves.setEdgeWeight(previousMove, currentMove, xMoves.getEdgeWeight(xMoves.getEdge(previousMove, currentMove)) - 1.0);
                    moves = moves.divide(BigInteger.valueOf(10));

                }
                lose++;
            }
        }
        moves = null;
    }

    public void recalculateO(TTTBoard board){
        int result = board.result(this.piece);
        List<Moves> Moves = oMoves.vertexSet().stream().toList();
        if(result > 0){
            while(!moves.equals(BigInteger.valueOf(0)) && !moves.divide(BigInteger.valueOf(10)).equals(BigInteger.valueOf(0))){

                Moves previousMove = Moves.stream().filter(o -> o.getMove().equals(moves.divide(BigInteger.valueOf(10)))).findFirst().get();
                Moves currentMove = Moves.stream().filter(o -> o.getMove().equals(moves)).findFirst().get();

                oMoves.setEdgeWeight(previousMove, currentMove, oMoves.getEdgeWeight(oMoves.getEdge(previousMove, currentMove)) + 1.0);
                moves = moves.divide(BigInteger.valueOf(10));

            }
            wins++;
        }else{
            if(result < 0) {
                while (!moves.equals(BigInteger.valueOf(0)) && !moves.divide(BigInteger.valueOf(10)).equals(BigInteger.valueOf(0))) {
                    Moves previousMove = Moves.stream().filter(o -> o.getMove().equals(moves.divide(BigInteger.valueOf(10)))).findFirst().get();
                    Moves currentMove = Moves.stream().filter(o -> o.getMove().equals(moves)).findFirst().get();

                    oMoves.setEdgeWeight(previousMove, currentMove, oMoves.getEdgeWeight(oMoves.getEdge(previousMove, currentMove)) - 1.0);
                    moves = moves.divide(BigInteger.valueOf(10));
                }
                lose++;
            }
        }
        moves = null;
    }

    public int move(TTTBoard board){
        if(this.piece.equals(TTTPiece.X)) {
            List<Integer> legalMoves = board.getLegalMoves();
            Moves currentMoves = new Moves(moves);
            int move;
            if (moves !=null && xMoves.containsVertex(currentMoves) && !xMoves.outgoingEdgesOf(currentMoves).stream().map(o -> (xMoves.getEdgeTarget(o))).collect(Collectors.toList()).isEmpty()) {
                move = computeX(board);
                moves = moves.multiply(BigInteger.valueOf(10));
                moves = moves.add(BigInteger.valueOf(move));
                move = move - 1;
                saveMovesX();
            } else {
                if (moves != null) {
                    moves = moves.multiply(BigInteger.valueOf(10));
                    move = (int) (Math.random() * legalMoves.size());
                    while (move == legalMoves.size()) {
                        move = (int) (Math.random() * legalMoves.size());
                    }
                    move = legalMoves.get(move);
                    moves = moves.add(BigInteger.valueOf(move + 1));
                    saveMovesX();
                } else {
                    move = (int) (Math.random() * legalMoves.size());
                    while (move == legalMoves.size()) {
                        move = (int) (Math.random() * legalMoves.size());
                    }
                    move = legalMoves.get(move);
                    moves = BigInteger.valueOf(move + 1);
                    saveMovesX();
                }
            }
            return move;
        }else{
            List<Integer> legalMoves = board.getLegalMoves();
            Moves currentMoves = new Moves(moves);
            int move;
            if (moves != null && oMoves.containsVertex(currentMoves) && !oMoves.outgoingEdgesOf(currentMoves).stream().map(o -> (oMoves.getEdgeTarget(o))).collect(Collectors.toList()).isEmpty()) {
                move = computeO(board);
                moves = moves.multiply(BigInteger.valueOf(10));
                moves = moves.add(BigInteger.valueOf(move));
                move = move - 1;
                saveMovesO();
            } else {
                if (moves != null) {
                    moves = moves.multiply(BigInteger.valueOf(10));
                    move = (int) (Math.random() * legalMoves.size());
                    while (move == legalMoves.size()) {
                        move = (int) (Math.random() * legalMoves.size());
                    }
                    move = legalMoves.get(move);
                    moves = moves.add(BigInteger.valueOf(move + 1));
                    saveMovesO();
                } else {
                    move = (int) (Math.random() * legalMoves.size());
                    while (move == legalMoves.size()) {
                        move = (int) (Math.random() * legalMoves.size());
                    }
                    move = legalMoves.get(move);
                    moves = BigInteger.valueOf(move + 1);
                    saveMovesO();
                }
            }
            return move;
        }
    }

    public void printStats(){
        System.out.println("Wins: " + wins);
        System.out.println("Lose: " + lose);
    }
}
