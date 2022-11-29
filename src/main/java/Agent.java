import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Agent {
    private DirectedAcyclicGraph<Moves, DefaultWeightedEdge> xMoves = new DirectedAcyclicGraph<Moves, DefaultWeightedEdge>(DefaultWeightedEdge.class);
    private DirectedAcyclicGraph<Moves, DefaultWeightedEdge> oMoves = new DirectedAcyclicGraph<Moves, DefaultWeightedEdge>(DefaultWeightedEdge.class);
    private TTTPiece piece;
    private BigInteger moves;
    private int chance;
    private int[] random;

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

    public TTTPiece getPiece() {
        return piece;
    }

    public void setPiece(TTTPiece piece) {
        this.piece = piece;
    }

    private void saveMoves(){
        Moves currentMoves = new Moves(moves);
        if(!xMoves.containsVertex(currentMoves)){
            xMoves.addVertex(currentMoves);
            if(xMoves.vertexSet().size() > 1) {
                List<Moves> previousMoves = xMoves.vertexSet().stream().toList();
                Moves previousMove = previousMoves.stream().filter(o -> {return o.getMove().equals(moves.divide(BigInteger.valueOf(10)));}).findFirst().get();
                xMoves.addEdge(previousMove, currentMoves);
                //xMoves.setEdgeWeight(previousMove, currentMoves, 362880.0);
            }
        }
    }

    private int compute(TTTBoard board){
        Moves currentMoves = new Moves(moves);
        Moves nextMove = xMoves.getDescendants(currentMoves).stream().max(Moves::compare).get();
        List<Moves> nextMoves = xMoves.getDescendants(currentMoves).stream().toList();
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
        if(random() == 1){
            nextMove = xMoves.getEdgeTarget(edge);
            return nextMove.getMove().mod(BigInteger.valueOf(10)).intValue();
        }else{
            ArrayList<Moves> newMovies = new ArrayList<>(xMoves.getDescendants(currentMoves));
            newMovies.remove(xMoves.getEdgeTarget(edge));
            List<Integer> newMoviess = newMovies.stream()
                    .map(Moves::getMove)
                    .map(o -> o.mod(BigInteger.valueOf(10)))
                    .map(BigInteger::intValue)
                    .toList();
            newMoviess.addAll(board.getLegalMoves());
            int move = (int) (Math.random() * newMoviess.size());
            while(move == 0){
                move = (int) (Math.random() * newMoviess.size());
            }
            return newMoviess.get(move);
        }
    }

    public void recalculate(TTTBoard board){
        int result = board.result(this.piece);
        if(result > 0){
            while(!moves.equals(BigInteger.valueOf(0))){
                List<Moves> Moves = xMoves.vertexSet().stream().toList();
                Moves previousMove = Moves.stream().filter(o -> {return o.getMove().equals(moves.divide(BigInteger.valueOf(10)));}).findFirst().get();
                Moves currentMove = Moves.stream().filter(o -> {return o.getMove().equals(moves);}).findFirst().get();

                DirectedWeightedMultigraph<Moves, DefaultWeightedEdge> xMovess = new DirectedWeightedMultigraph<Moves, DefaultWeightedEdge>(DefaultWeightedEdge.class);
                xMovess.addVertex(previousMove);
                xMovess.addVertex(currentMove);
                xMovess.addEdge(previousMove, currentMove);
                xMovess.setEdgeWeight(previousMove, currentMove, xMoves.getEdgeWeight(xMoves.getEdge(previousMove, currentMove)) + 1.0);

                xMoves.setEdgeWeight(previousMove, currentMove, xMoves.getEdgeWeight(xMoves.getEdge(previousMove, currentMove)) + 1.0);
                moves = moves.divide(BigInteger.valueOf(10));
                System.out.println();
            }
        }else{
            if(result < 0) {
                while (!moves.equals(BigInteger.valueOf(0))) {
                    List<Moves> Moves = xMoves.vertexSet().stream().toList();
                    Moves previousMove = Moves.stream().filter(o -> {return o.getMove().equals(moves.divide(BigInteger.valueOf(10)));}).findFirst().get();
                    Moves currentMove = Moves.stream().filter(o -> {return o.getMove().equals(moves);}).findFirst().get();

                    DirectedWeightedMultigraph<Moves, DefaultWeightedEdge> xMovess = new DirectedWeightedMultigraph<Moves, DefaultWeightedEdge>(DefaultWeightedEdge.class);
                    xMovess.addVertex(previousMove);
                    xMovess.addVertex(currentMove);
                    xMovess.addEdge(previousMove, currentMove);
                    xMovess.setEdgeWeight(previousMove, currentMove, xMoves.getEdgeWeight(xMoves.getEdge(previousMove, currentMove)) + 1.0);

                    xMoves.setEdgeWeight(previousMove, currentMove, xMoves.getEdgeWeight(xMoves.getEdge(previousMove, currentMove)) - 1.0);
                    moves = moves.divide(BigInteger.valueOf(10));
                    System.out.println();
                }
            }
        }
    }

    public int move(TTTBoard board){
        List<Integer> legalMoves = board.getLegalMoves();
        Moves currentMoves = new Moves(moves);
        int move;
        if(xMoves.containsVertex(currentMoves) && !xMoves.getDescendants(currentMoves).isEmpty()){
            move = compute(board);
            moves = moves.multiply(BigInteger.valueOf(10));
            moves = moves.add(BigInteger.valueOf(move));
            saveMoves();
        }else{
            if(moves != null) {
                moves = moves.multiply(BigInteger.valueOf(10));
                move = (int) (Math.random() * legalMoves.size());
                while (move == legalMoves.size()) {
                    move = (int) (Math.random() * legalMoves.size());
                }
                move = legalMoves.get(move);
                moves = moves.add(BigInteger.valueOf(move));
                saveMoves();
            }else{
                move = (int) (Math.random() * legalMoves.size());
                while (move == legalMoves.size()) {
                    move = (int) (Math.random() * legalMoves.size());
                }
                move = legalMoves.get(move);
                moves = BigInteger.valueOf(move);
                saveMoves();
            }
        }
        return move;
    }
}
