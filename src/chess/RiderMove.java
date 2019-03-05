package chess;

public class RiderMove extends ChessMove {

	public RiderMove(int color, Position from, Position to,
			SideEffect... effects) {
		super(color, from, to, effects);
	}

	@Override
	public boolean isPseudoLegal(ChessNode node) {
		return !node.isAlly(color, to);
	}

	@Override
	public boolean nextIsPseudoLegal(ChessNode node) {
		return node.isEmpty(to);
	}

}
