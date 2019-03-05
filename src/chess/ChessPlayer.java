package chess;

import ai.Player;

public abstract class ChessPlayer extends Player<ChessMove, ChessNode> {

	public ChessPlayer(int color) {
		super(color);
	}

}
