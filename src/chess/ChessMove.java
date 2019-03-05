package chess;

import java.util.Collections;
import java.util.Vector;

import ai.Move;

public abstract class ChessMove implements Move<ChessNode> {

	protected int color;
	protected Position from;
	protected Position to;
	private Vector<SideEffect> effects;

	public ChessMove(int color, Position from, Position to,
			SideEffect... effects) {
		this.color = color;

		this.from = from;
		this.to = to;

		this.effects = new Vector<>();
		Collections.addAll(this.effects, effects);
	}

	@Override
	public boolean isLegal(ChessNode node) {
		ChessNode next = node.apply(this);
		for (ChessMove move : next.getPseudoLegalMoves()) {
			Piece piece = next.get(move.getTo());
			if (piece != null) {
				if (piece.getType().equals(new King())) {
					return false;
				}
			}
		}
		return true;
	}

	public abstract boolean isPseudoLegal(ChessNode node);

	public boolean nextIsPseudoLegal(ChessNode node) {
		return true;
	}

	@Override
	public void apply(ChessNode node) {
		if (!node.isEmpty(to)) {
			node.resetFiftyMoveCount();
		}
		node.move(from, to);
		for (SideEffect effect : effects) {
			effect.apply(node);
		}
	}

	public Position getFrom() {
		return from;
	}

	public Position getTo() {
		return to;
	}

	public void addSideEffects(SideEffect... effects) {
		Collections.addAll(this.effects, effects);
	}

	public Promotion getPromotion() {
		for (SideEffect effect : effects) {
			if (effect instanceof Promotion) {
				return (Promotion) effect;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return from + "" + to;
	}

}
