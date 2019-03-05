package ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class TranspositionPlayer<M extends Move<N>, N extends Node<M, N>>
		extends Player<M, N> {

	private M bestFirstMove;
	private int millis;
	private TranspositionTable<M, N> table;

	public TranspositionPlayer(int color, int millis) {
		super(color);
		this.millis = millis;
		table = new TranspositionTable<>(1000000);
	}

	@Override
	public M getMove(N node) {
		return iterativeDeepening(node);
	}

	private M iterativeDeepening(N node) {
		bestFirstMove = null;
		int depth = 1;
		long end = System.currentTimeMillis() + millis;
		do {
			negamax(node, depth++);
		} while (System.currentTimeMillis() < end);
		System.out.println("Transposition: " + depth);
		return bestFirstMove;
	}

	private void negamax(N node, int depth) {
		negamax(node, color, depth, Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY, true);
	}

	private double negamax(N node, int color, int depth, double alpha,
			double beta, boolean isRoot) {
		double alphaOrig = alpha;

		TranspositionEntry<M> entry = table.get(node);
		M bestMove = null;
		if (entry != null && entry.getDepth() >= depth) {
			switch (entry.getFlag()) {
			case TranspositionEntry.EXACT:
				return entry.getValue();
			case TranspositionEntry.LOWER_BOUND:
				alpha = Math.max(alpha, entry.getValue());
				break;
			case TranspositionEntry.UPPER_BOUND:
				beta = Math.min(beta, entry.getValue());
				break;
			}

			bestMove = entry.getBestMove();
		}

		if (depth == 0 || node.gameOver()) {
			return color * node.getValue(color);
		}

		double bestValue = Double.NEGATIVE_INFINITY;
		List<Pair<N, M>> pairs = getSortedMoves(node, color);
		if (bestMove != null && bestMove.isLegal(node)) {
			pairs.add(0, new Pair<>(node.apply(bestMove), bestMove));
		}
		for (Pair<N, M> pair : pairs) {
			double val = -negamax(pair.getKey(), -color, depth - 1, -beta,
					-alpha, false);

			if (val > bestValue) {
				bestValue = val;
				bestMove = pair.getValue();
			}

			if (val > alpha) {
				alpha = val;
				if (isRoot) {
					bestFirstMove = pair.getValue();
				}
			}
			if (alpha >= beta) {
				break;
			}
		}

		int flag;
		if (bestValue <= alphaOrig) {
			flag = TranspositionEntry.UPPER_BOUND;
		} else if (bestValue >= beta) {
			flag = TranspositionEntry.LOWER_BOUND;
		} else {
			flag = TranspositionEntry.EXACT;
		}
		table.put(node, new TranspositionEntry<>(node.getZobristHash(),
				bestMove, bestValue, depth, flag));

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
