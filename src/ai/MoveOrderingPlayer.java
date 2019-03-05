package ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class MoveOrderingPlayer<M extends Move<N>, N extends Node<M, N>>
		extends Player<M, N> {

	private M bestMove;
	private int millis;

	public MoveOrderingPlayer(int color, int millis) {
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
			negamax(node, depth++);
		} while (System.currentTimeMillis() < end);
		System.out.println("Move Ordering: " + depth);
		return bestMove;
	}

	private void negamax(N node, int depth) {
		negamax(node, color, depth, Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY, true);
	}

	private double negamax(N node, int color, int depth, double alpha,
			double beta, boolean isRoot) {
		if (depth == 0 || node.gameOver()) {
			return color * node.getValue(color);
		}

		List<Pair<N, M>> pairs = getSortedMoves(node, color);
		double bestValue = Double.NEGATIVE_INFINITY;
		for (Pair<N, M> pair : pairs) {
			double val = -negamax(pair.getKey(), -color, depth - 1, -beta,
					-alpha, false);
			bestValue = Math.max(bestValue, val);
			if (val > alpha) {
				alpha = val;
				if (isRoot) {
					bestMove = pair.getValue();
				}
			}
			if (alpha >= beta) {
				return bestValue;
			}
		}
		return bestValue;
	}

	private List<Pair<N, M>> getSortedMoves(N node, final int color) {
		List<Pair<N, M>> pairs = new Vector<>();
		for (M move : node.getLegalMoves()) {
			pairs.add(new Pair<>(node.apply(move), move));
		}

		Collections.sort(pairs, new Comparator<Pair<N, M>>() {
			@Override
			public int compare(Pair<N, M> o1, Pair<N, M> o2) {
				return color
						* (o2.getKey().getEstimate() - o1.getKey()
								.getEstimate());
			}
		});

		return pairs;
	}

}
