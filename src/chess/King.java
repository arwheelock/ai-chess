package chess;

public class King extends Type {

	@Override
	public MoveList getMoves(int color, Position pos) {
		MoveList moves = new MoveList();

		moves.add(makeMove(color, pos, pos.offset(color, 1, 0)));
		moves.add(makeMove(color, pos, pos.offset(color, 0, 1)));
		moves.add(makeMove(color, pos, pos.offset(color, -1, 0)));
		moves.add(makeMove(color, pos, pos.offset(color, 0, -1)));

		moves.add(makeMove(color, pos, pos.offset(color, 1, 1)));
		moves.add(makeMove(color, pos, pos.offset(color, -1, 1)));
		moves.add(makeMove(color, pos, pos.offset(color, -1, -1)));
		moves.add(makeMove(color, pos, pos.offset(color, 1, -1)));

		if (pos.relativeTo(color).equals(new Position("e1"))) {
			moves.add(new Castling(color, pos, Castling.KINGSIDE));
			moves.add(new Castling(color, pos, Castling.QUEENSIDE));
		}

		return moves;
	}

	private ChessMove makeMove(int color, Position from, Position to) {
		ChessMove move = new LeaperMove(color, from, to);
		if (from.relativeTo(color).equals(new Position("e1"))) {
			move.addSideEffects(new ProhibitCastling(color, Castling.KINGSIDE),
					new ProhibitCastling(color, Castling.QUEENSIDE));
		}
		return move;
	}

	@Override
	public int getValue() {
		return KING_VALUE;
	}

	@Override
	public int getIndex() {
		return KING;
	}

}
