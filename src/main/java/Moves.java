import java.math.BigInteger;

public class Moves{
    private BigInteger move;
    private int weight;

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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int wight) {
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

    public static int compare (Moves m1, Moves m2){
        if(m1.getWeight() > m2.getWeight())
            return 1;
        return -1;
    }
}
