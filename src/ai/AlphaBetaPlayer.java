package ai;

import java.util.Collections;
import java.util.List;

public class AlphaBetaPlayer<M extends Move<N>, N extends Node<M, N>> extends
		Player<M, N> {

	private M bestMove;
	private int millis;

	public AlphaBetaPlayer(int color, int millis) {
		super(color);
		this.millis = millis;
	}

	@Override
	public M getMove(N node) {
		return iterativeDeepening(node);
	}

	private M iterativeDeepening(N node) {
		bestMove = null;
		int depth = 1;
		long end = System.currentTimeMillis() + millis;
		do {
			alphaBeta(node, depth++);
		} while (System.currentTimeMillis() < end);
		return bestMove;
	}

	private void alphaBeta(N node, int depth) {
		alphaBeta(node, color, depth, Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY, true);
	}

	private double alphaBeta(N node, int color, int depth, double alpha,
			double beta, boolean isRoot) {
		if (depth == 0 || node.gameOver()) {
			return node.getValue(color);
		}

		List<M> moves = node.getLegalMoves();
		Collections.shuffle(moves);

		if (color == Player.WHITE) {
			for (M move : moves) {
				double result = alphaBeta(node.apply(move), -color, depth - 1,
						alpha, beta, false);
				if (result > alpha) {
					alpha = result;
					if (isRoot) {
						bestMove = move;
					}
				}

				if (alpha >= beta) {
					return alpha;
				}
			}
			return alpha;
		} else {
			for (M move : moves) {
				double result = alphaBeta(node.apply(move), -color, depth - 1,
						alpha, beta, false);
				if (result < beta) {
					beta = result;
					if (isRoot) {
						bestMove = move;
					}
				}

				if (beta <= alpha) {
					return beta;
				}
			}
			return beta;
		}
	}

}
