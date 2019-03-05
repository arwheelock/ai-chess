package chess;

public class PawnMove extends ChessMove {

	public PawnMove(int color, Position from, Position to,
			SideEffect... effects) {
		super(color, from, to, effects);
	}

	@Override
	public void apply(ChessNode node) {
		super.apply(node);
		node.resetFiftyMoveCount();
	}

	@Override
	public boolean isPseudoLegal(ChessNode node) {
		return node.isEmpty(to);
	}

	@Override
	public boolean nextIsPseudoLegal(ChessNode node) {
		return isPseudoLegal(node);
	}

}
