package chess;

public class Castling extends ChessMove {

	public static final int KINGSIDE = 1;
	public static final int QUEENSIDE = -1;

	private int side;

	public Castling(int color, Position from, int side) {
		super(color, from, from.offset(color, 0, side == KINGSIDE ? 2 : -2));
		this.side = side;

	}

	@Override
	public void apply(ChessNode node) {
		super.apply(node);
		int fromColumn = side == Castling.KINGSIDE ? 7 : 0;
		Position rookFrom = new Position(7, fromColumn).relativeTo(color);
		Position rookTo = rookFrom.offset(color, 0,
				side == Castling.KINGSIDE ? -2 : 3);
		node.move(rookFrom, rookTo);
		node.prohibitCastling(color, Castling.KINGSIDE);
		node.prohibitCastling(color, Castling.QUEENSIDE);
	}

	@Override
	public boolean isLegal(ChessNode node) {
		return !node.inCheck()
				&& new LeaperMove(color, from, from.offset(color, 0, side))
						.isLegal(node) && super.isLegal(node);
	}

	@Override
	public boolean isPseudoLegal(ChessNode node) {
		if (!node.canCastle(color, side)) {
			return false;
		}

		int toCheck = side == KINGSIDE ? 3 : 4;
		for (int i = 1; i < toCheck; i++) {
			if (!node.isEmpty(from.offset(color, 0, i * side))) {
				return false;
			}
		}

		Position rookPos = from.offset(color, 0, toCheck * side);
		return !(node.isEmpty(rookPos) || node.get(rookPos).getColor() != color);

	}

	public static int castlingIndex(int side) {
		return (side + 1) / 2;
	}

}
