import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TTTBoard implements Board<Integer> {
    private static final int NUM_SQUARES = 9;
    private TTTPiece[] position;
    private TTTPiece turn;
    public TTTBoard(TTTPiece[] position, TTTPiece turn) {
        this.position = position;
        this.turn = turn;
    }
    public TTTBoard() {
        // по умолчанию в начале игры поле пустое
        position = new TTTPiece[NUM_SQUARES];
        Arrays.fill(position, TTTPiece.E);
        // Первый игрок ставит X
        turn = TTTPiece.X;
    }
    @Override
    public Piece getTurn() {
        return turn;
    }

    @Override
    public TTTBoard move(Integer location) {
        TTTPiece[] tempPosition = Arrays.copyOf(position, position.length);
        tempPosition[location] = turn;
        return new TTTBoard(tempPosition, turn.opposite());
    }

    @Override
    public List<Integer> getLegalMoves() {
        ArrayList<Integer> legalMoves = new ArrayList<>();
        for (int i = 0; i < NUM_SQUARES; i++) {
            // пустые клетки — допустимые ходы
            if (position[i] == TTTPiece.E) {
                legalMoves.add(i);
            }
        }
        return legalMoves;
    }

    @Override
    public boolean isWin() {
        // проверяем три строки, три столбца и две диагонали
        return
                checkPos(0, 1, 2) || checkPos(3, 4, 5) || checkPos(6, 7, 8)
                        || checkPos(0, 3, 6) || checkPos(1, 4, 7) || checkPos(2, 5, 8)
                        || checkPos(0, 4, 8) || checkPos(2, 4, 6);
    }
    public int result(TTTPiece piece){
        if(isWin()){
            if((checkPos(0, 1, 2) && position[0].equals(piece) && position[1].equals(piece) && position[2].equals(piece))
                    || (checkPos(3, 4, 5) && position[3].equals(piece) && position[4].equals(piece) && position[5].equals(piece))
                    || (checkPos(6, 7, 8) && position[6].equals(piece) && position[7].equals(piece) && position[8].equals(piece))
                    || (checkPos(0, 3, 6) && position[0].equals(piece) && position[3].equals(piece) && position[6].equals(piece))
                    || (checkPos(1, 4, 7) && position[1].equals(piece) && position[4].equals(piece) && position[7].equals(piece))
                    || (checkPos(2, 5, 8) && position[2].equals(piece) && position[5].equals(piece) && position[8].equals(piece))
                    || (checkPos(0, 4, 8) && position[0].equals(piece) && position[4].equals(piece) && position[8].equals(piece))
                    || (checkPos(2, 4, 6) && position[2].equals(piece) && position[4].equals(piece) && position[6].equals(piece))){
                return 1;
            }else{
                return -1;
            }
        }else {
            return 0;
        }
    }
    private boolean checkPos(int p0, int p1, int p2) {
        return position[p0] == position[p1] && position[p0] ==
                position[p2] && position[p0] != TTTPiece.E;
    }

    @Override
    public double evaluate(Piece player) {
        if (isWin() && turn == player) {
            return -1;
        } else if (isWin() && turn != player) {
            return 1;
        } else {
            return 0.0;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                sb.append(position[row * 3 + col].toString());
                if (col != 2) {
                    sb.append("|");
                }
            }
            sb.append(System.lineSeparator());
            if (row != 2) {
                sb.append("-----");
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}