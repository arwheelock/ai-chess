package chess;

public class ProhibitCastling implements SideEffect {

	private int color;
	private int side;

	public ProhibitCastling(int color, int side) {
		this.color = color;
		this.side = side;
	}

	@Override
	public void apply(ChessNode node) {
		node.prohibitCastling(color, side);
	}

}