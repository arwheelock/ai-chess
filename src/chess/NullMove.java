package chess;

public class NullMove extends ChessMove {

	public NullMove(int color) {
		super(color, null, null);
	}

	@Override
	public boolean isPseudoLegal(ChessNode node) {
		return true;
	}

	@Override
	public void apply(ChessNode node) {
	}

}
