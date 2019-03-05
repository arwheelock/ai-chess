package chess;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PieceSquareTable {

	private static final int[][][] table = new int[Type.COUNT + 1][ChessNode.LENGTH][ChessNode.LENGTH];

	static {
		String prefix = "res" + File.separator + "piece-square-tables"
				+ File.separator;
		table[Type.PAWN] = readTable(prefix + "pawn.txt");
		table[Type.KNIGHT] = readTable(prefix + "knight.txt");
		table[Type.BISHOP] = readTable(prefix + "bishop.txt");
		table[Type.ROOK] = readTable(prefix + "rook.txt");
		table[Type.QUEEN] = readTable(prefix + "queen.txt");
		table[Type.KING] = readTable(prefix + "king-middle.txt");
		table[Type.KING + 1] = readTable(prefix + "king-end.txt");
	}

	private static int[][] readTable(String file) {
		try (Scanner in = new Scanner(new File(file))) {
			int[][] table = new int[ChessNode.LENGTH][ChessNode.LENGTH];
			for (int i = 0; i < table.length; i++) {
				for (int j = 0; j < table[0].length; j++) {
					table[i][j] = in.nextInt();
				}
			}
			return table;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	public static int getValue(Piece piece, Position pos, ChessNode node) {
		return getValue(piece.getColor(), piece.getType(), pos, node);
	}

	private static int getValue(int color, Type type, Position pos,
			ChessNode node) {
		Position relative = pos.relativeTo(color);
		int index = type.getIndex();
		if (index == Type.KING && node.inEndgame()) {
			index++;
		}
		return color * table[index][relative.getRow()][relative.getColumn()];
	}

}
