package chess;

public class Queen extends Rider {

	@Override
	public MoveList getMoves(int color, Position pos) {
		MoveList moves = new MoveList();

		moves.add(getRiderMoves(color, pos, new Position(1, 0)));
		moves.add(getRiderMoves(color, pos, new Position(0, 1)));
		moves.add(getRiderMoves(color, pos, new Position(-1, 0)));
		moves.add(getRiderMoves(color, pos, new Position(0, -1)));

		moves.add(getRiderMoves(color, pos, new Position(1, 1)));
		moves.add(getRiderMoves(color, pos, new Position(-1, 1)));
		moves.add(getRiderMoves(color, pos, new Position(-1, -1)));
		moves.add(getRiderMoves(color, pos, new Position(1, -1)));

		return moves;
	}

	@Override
	public int getValue() {
		return QUEEN_VALUE;
	}

	@Override
	public int getIndex() {
		return QUEEN;
	}

}
