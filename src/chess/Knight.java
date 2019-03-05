package chess;

public class Knight extends Type {

	@Override
	public MoveList getMoves(int color, Position pos) {
		MoveList moves = new MoveList();

		moves.add(new LeaperMove(color, pos, pos.offset(color, 2, 1)));
		moves.add(new LeaperMove(color, pos, pos.offset(color, 1, 2)));
		moves.add(new LeaperMove(color, pos, pos.offset(color, -1, 2)));
		moves.add(new LeaperMove(color, pos, pos.offset(color, -2, 1)));
		moves.add(new LeaperMove(color, pos, pos.offset(color, -2, -1)));
		moves.add(new LeaperMove(color, pos, pos.offset(color, -1, -2)));
		moves.add(new LeaperMove(color, pos, pos.offset(color, 1, -2)));
		moves.add(new LeaperMove(color, pos, pos.offset(color, 2, -1)));

		return moves;
	}

	@Override
	public int getValue() {
		return KNIGHT_VALUE;
	}

	@Override
	public int getIndex() {
		return KNIGHT;
	}

}
