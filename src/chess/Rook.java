package chess;

import java.util.List;

public class Rook extends Rider {

	@Override
	public MoveList getMoves(int color, Position pos) {
		MoveList moves = new MoveList();

		moves.add(makeMoves(color, pos, new Position(1, 0)));
		moves.add(makeMoves(color, pos, new Position(0, 1)));
		moves.add(makeMoves(color, pos, new Position(-1, 0)));
		moves.add(makeMoves(color, pos, new Position(0, -1)));

		return moves;
	}

	private List<ChessMove> makeMoves(int color, Position from, Position delta) {
		List<ChessMove> moves = getRiderMoves(color, from, delta);

		if (from.relativeTo(color).equals(new Position("a1"))) {
			for (ChessMove move : moves) {
				move.addSideEffects(new ProhibitCastling(color,
						Castling.QUEENSIDE));
			}
		} else if (from.relativeTo(color).equals(new Position("h1"))) {
			for (ChessMove move : moves) {
				move.addSideEffects(new ProhibitCastling(color,
						Castling.KINGSIDE));
			}
		}

		return moves;
	}

	@Override
	public int getValue() {
		return ROOK_VALUE;
	}

	@Override
	public int getIndex() {
		return ROOK;
	}

}
