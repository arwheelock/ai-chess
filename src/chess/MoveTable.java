package chess;

import static ai.Player.BLACK;
import static ai.Player.WHITE;
import static ai.Player.colorIndex;

public class MoveTable {

	private static final MoveList[][][][] moves = new MoveList[2][Type.COUNT][ChessNode.LENGTH][ChessNode.LENGTH];

	static {
		add(WHITE);
		add(BLACK);
	}

	private static void add(int color) {
		add(color, new Pawn());
		add(color, new Knight());
		add(color, new Bishop());
		add(color, new Rook());
		add(color, new Queen());
		add(color, new King());
	}

	private static void add(int color, Type type) {
		for (int i = 0; i < ChessNode.LENGTH; i++) {
			for (int j = 0; j < ChessNode.LENGTH; j++) {
				moves[colorIndex(color)][type.getIndex()][i][j] = type
						.getMoves(color, new Position(i, j));
			}
		}
	}

	public static MoveList get(int color, Type type, Position pos) {
		return moves[colorIndex(color)][type.getIndex()][pos.getRow()][pos
				.getColumn()];
	}

}
