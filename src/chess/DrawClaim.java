package chess;

import java.util.List;

public class DrawClaim extends ChessMove {

	private static final int FIFTY_MOVE_LIMIT = 50;
	private static final int REPETITION_LIMIT = 3;

	public DrawClaim(int color) {
		super(color, null, null);
	}

	@Override
	public boolean isLegal(ChessNode node) {
		return limitReached(node) || threefoldRepetition(node)
				|| limitReachedNext(node) || threefoldRepetitionNext(node);
	}

	@Override
	public boolean isPseudoLegal(ChessNode node) {
		return true;
	}

	private boolean limitReached(ChessNode node) {
		return node.getFiftyMoveCount() >= FIFTY_MOVE_LIMIT;
	}

	private boolean limitReachedNext(ChessNode node) {
		if (node.getFiftyMoveCount() == FIFTY_MOVE_LIMIT - 1) {
			for (ChessMove move : node.getLegalMoves()) {
				if (limitReached(node.apply(move))) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean threefoldRepetition(ChessNode node) {
		List<ChessNode> history = node.getHistory();
		int count = 1;
		for (ChessNode past : history) {
			if (node.equals(past)) {
				if (++count == REPETITION_LIMIT) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean threefoldRepetitionNext(ChessNode node) {
		for (ChessMove move : node.getLegalMoves()) {
			if (threefoldRepetition(node.apply(move))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void apply(ChessNode node) {
		node.claimDraw();
	}

}
