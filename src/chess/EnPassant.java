package chess;

public class EnPassant extends ChessMove {

	private Position toRemove;

	public EnPassant(int color, Position from, Position to) {
		super(color, from, to);
		toRemove = to.offset(color, -1, 0);
	}

	@Override
	public void apply(ChessNode node) {
		super.apply(node);
		node.remove(toRemove);
		node.resetFiftyMoveCount();
	}

	@Override
	public boolean isPseudoLegal(ChessNode node) {
		Position enPassant = node.getEnPassant();
		return enPassant != null && enPassant.equals(to);
	}

}
