package chess;

public class PawnCapture extends ChessMove {

	public PawnCapture(int color, Position from, Position to,
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
		return node.isEnemy(color, to);
	}

}
