import java.io.Serializable;
import java.math.BigInteger;

public class Moves implements Serializable {
    private BigInteger move;
    private double weight;

    public Moves(BigInteger move){
        this.move = move;
    }

    public Moves(BigInteger move, int weight){
        this.move = move;
        this.weight = weight;
    }

    public BigInteger getMove() {
        return move;
    }

    public void setMove(BigInteger move) {
        this.move = move;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double wight) {
        this.weight = wight;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Moves){
            return ((Moves) obj).getMove().equals(move);
        }else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return move.hashCode();
    }

    public static int compare (Moves m1, Moves m2){
        if(m1.getWeight() > m2.getWeight())
            return 1;
        return -1;
    }
}
