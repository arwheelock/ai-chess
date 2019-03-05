package chess;

public class LeaperMove extends ChessMove {

	public LeaperMove(int color, Position from, Position to,
			SideEffect... effects) {
		super(color, from, to, effects);
	}

	@Override
	public boolean isPseudoLegal(ChessNode node) {
		return !node.isAlly(color, to);
	}

}
