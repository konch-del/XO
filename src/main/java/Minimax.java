public class Minimax {

    public static <Move> double minimax(Board<Move> board,
                                        Boolean maximizing, Piece originalPlayer, int maxDepth) {
        if (board.isWin() || board.isDraw() || maxDepth == 0) {
            return board.evaluate(originalPlayer);
        }
        if (maximizing) {
            double bestEval = Double.NEGATIVE_INFINITY;
            for (Move move : board.getLegalMoves()) {
                double result = minimax(board.move(move), false,
                        originalPlayer, maxDepth - 1);
                bestEval = Math.max(result, bestEval);
            }
            return bestEval;
        } else {
            double worstEval = Double.POSITIVE_INFINITY;
            for (Move move : board.getLegalMoves()) {
                double result = minimax(board.move(move), true,
                        originalPlayer, maxDepth - 1);
                worstEval = Math.min(result, worstEval);
            }
            return worstEval;
        }
    }

    public static <Move> Move findBestMove(Board<Move> board, int maxDepth) {
        double bestEval = Double.NEGATIVE_INFINITY;
        Move bestMove = null;
        for (Move move : board.getLegalMoves()) {
            double result = minimax(board.move(move), false,
                    board.getTurn(), maxDepth);
            if (result > bestEval) {
                bestEval = result;
                bestMove = move;
            }
        }
        return bestMove;
    }
}