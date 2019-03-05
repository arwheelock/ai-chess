package chess;

public class Promotion implements SideEffect {

	private int color;
	private Position pos;
	private Type promoteTo;

	public Promotion(int color, Position pos, Type promoteTo) {
		this.color = color;
		this.pos = pos;
		this.promoteTo = promoteTo;
	}

	@Override
	public void apply(ChessNode node) {
		node.set(pos, new Piece(promoteTo, color));
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Promotion) {
			Promotion o = (Promotion) other;
			return pos.equals(o.pos) && promoteTo.equals(o.promoteTo);
		}
		return false;
	}

}