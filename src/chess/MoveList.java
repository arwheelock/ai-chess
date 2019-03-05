package chess;

import java.util.List;
import java.util.Vector;

public class MoveList {

	private List<ChessMove> independent;
	private List<List<ChessMove>> dependent;

	public MoveList() {
		independent = new Vector<>();
		dependent = new Vector<>();
	}

	public void add(ChessMove move) {
		if (move.getTo().inRange()) {
			independent.add(move);
		}
	}

	public void add(List<ChessMove> moves) {
		List<ChessMove> inRange = new Vector<>();
		for (int i = 0; i < moves.size() && moves.get(i).getTo().inRange(); i++) {
			inRange.add(moves.get(i));
		}
		dependent.add(inRange);
	}

	public List<ChessMove> getPseudoLegal(ChessNode node) {
		List<ChessMove> pseudoLegal = new Vector<>();

		for (ChessMove move : independent) {
			if (move.isPseudoLegal(node)) {
				pseudoLegal.add(move);
			}
		}

		for (List<ChessMove> moves : dependent) {
			boolean nextIsPseudoLegal = true;

			for (int i = 0; nextIsPseudoLegal && i < moves.size(); i++) {
				ChessMove move = moves.get(i);

				if (move.isPseudoLegal(node)) {
					pseudoLegal.add(move);
					nextIsPseudoLegal = move.nextIsPseudoLegal(node);
				} else {
					nextIsPseudoLegal = false;
				}
			}
		}

		return pseudoLegal;
	}

	@Override
	public String toString() {
		return independent + " " + dependent;
	}

}
