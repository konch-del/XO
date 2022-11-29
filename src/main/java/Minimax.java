public class Minimax {
    // Находим наилучший из возможных результатов для первого игрока
    public static <Move> double minimax(Board<Move> board,
                                        Boolean maximizing, Piece originalPlayer, int maxDepth) {
        // Базовый случай — достигнута финальная
        // позиция или максимальная глубина поиска
        if (board.isWin() || board.isDraw() || maxDepth == 0) {
            return board.evaluate(originalPlayer);
        }
        // Рекурсивный случай — максимизируйте свою выгоду или
        // минимизируйте выгоду противника
        if (maximizing) {
            double bestEval = Double.NEGATIVE_INFINITY; // результат выше
            for (Move move : board.getLegalMoves()) {
                double result = minimax(board.move(move), false,
                        originalPlayer, maxDepth - 1);
                bestEval = Math.max(result, bestEval);
            }
            return bestEval;
        } else { // минимизация
            double worstEval = Double.POSITIVE_INFINITY; // результат ниже
            for (Move move : board.getLegalMoves()) {
                double result = minimax(board.move(move), true,
                        originalPlayer, maxDepth - 1);
                worstEval = Math.min(result, worstEval);
            }
            return worstEval;
        }
    }

    // Найти наилучший возможный ход из текущей
    // позиции, просматривая maxDepth ходов вперед
    public static <Move> Move findBestMove(Board<Move> board, int maxDepth) {
        double bestEval = Double.NEGATIVE_INFINITY;
        Move bestMove = null; // Наверняка не примет значение null
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