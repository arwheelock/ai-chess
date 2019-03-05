package chess;

import static ai.Player.colorIndex;

import java.util.Random;

public class ZobristTable {

	private static final Random rand = new Random();

	private static final long[][][][] board = new long[2][Type.COUNT][ChessNode.LENGTH][ChessNode.LENGTH];

	private static final long color = rand.nextLong();
	private static final long[][] canCastle = new long[2][2];
	private static final long[] enPassant = new long[ChessNode.LENGTH];

	static {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				for (int k = 0; k < board[0][0].length; k++) {
					for (int l = 0; l < board[0][0][0].length; l++) {
						board[i][j][k][l] = rand.nextLong();
					}
				}
			}
		}

		for (int i = 0; i < canCastle.length; i++) {
			for (int j = 0; j < canCastle[0].length; j++) {
				canCastle[i][j] = rand.nextLong();
			}
		}

		for (int i = 0; i < enPassant.length; i++) {
			enPassant[i] = rand.nextLong();
		}
	}

	public static long get(Position pos, Piece piece) {
		return board[colorIndex(piece.getColor())][piece.getType().getIndex()][pos
				.getRow()][pos.getColumn()];
	}

	public static long color() {
		return color;
	}

	public static long canCastle(int color, int side) {
		return canCastle[colorIndex(color)][Castling.castlingIndex(side)];
	}

	public static long enPassant(int column) {
		return enPassant[column];
	}

}
