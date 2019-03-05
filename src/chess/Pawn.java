package chess;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class Pawn extends Type {

	@Override
	public int getIndex() {
		return PAWN;
	}

	@Override
	public MoveList getMoves(int color, Position pos) {
		MoveList moves = new MoveList();

		if (pos.getRank(color) == 2) {
			moves.add(Arrays.asList(new ChessMove[] {
					new PawnMove(color, pos, pos.offset(color, 1, 0)),
					new PawnMove(color, pos, pos.offset(color, 2, 0),
							new SetEnPassant(pos.offset(color, 1, 0))) }));
		} else if (pos.getRank(color) == 7) {
			for (ChessMove move : getPromotions(color, pos,
					pos.offset(color, 1, 0), false)) {
				moves.add(move);
			}
		} else {
			moves.add(new PawnMove(color, pos, pos.offset(color, 1, 0)));
		}

		if (pos.getRank(color) == 7) {
			for (ChessMove move : getPromotions(color, pos,
					pos.offset(color, 1, 1), true)) {
				moves.add(move);
			}
			for (ChessMove move : getPromotions(color, pos,
					pos.offset(color, 1, -1), true)) {
				moves.add(move);
			}
		} else {
			moves.add(new PawnCapture(color, pos, pos.offset(color, 1, 1)));
			moves.add(new PawnCapture(color, pos, pos.offset(color, 1, -1)));
		}

		if (pos.getRank(color) == 5) {
			moves.add(new EnPassant(color, pos, pos.offset(color, 1, 1)));
			moves.add(new EnPassant(color, pos, pos.offset(color, 1, -1)));
		}

		return moves;
	}

	@Override
	public int getValue() {
		return PAWN_VALUE;
	}

	private List<ChessMove> getPromotions(int color, Position from,
			Position to, boolean capture) {
		List<ChessMove> promotions = new Vector<>();

		for (int i = Type.KNIGHT; i <= Type.QUEEN; i++) {
			if (capture) {
				promotions.add(new PawnCapture(color, from, to, new Promotion(
						color, to, Type.fromIndex(i))));
			} else {
				promotions.add(new PawnMove(color, from, to, new Promotion(
						color, to, Type.fromIndex(i))));
			}
		}

		return promotions;
	}

}
