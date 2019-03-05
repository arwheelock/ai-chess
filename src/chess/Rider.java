package chess;

import java.util.List;
import java.util.Vector;

public abstract class Rider extends Type {

	protected List<ChessMove> getRiderMoves(int color, Position from,
			Position delta) {
		List<ChessMove> moves = new Vector<>();
		Position to = from.offset(color, delta);
		for (int i = 1; i < ChessNode.LENGTH; i++) {
			moves.add(new RiderMove(color, from, to));
			to = to.offset(color, delta);
		}
		return moves;
	}

}