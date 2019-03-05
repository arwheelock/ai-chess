package chess;

import static ai.Player.WHITE;

public class Position {

	private int row;
	private int column;

	public Position(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public Position(String str) {
		this.row = ChessNode.LENGTH - (str.charAt(1) - '0');
		this.column = str.charAt(0) - 'a';
	}

	public Position relativeTo(int color) {
		return new Position(color == WHITE ? row : ChessNode.LENGTH - 1 - row,
				column);
	}

	public int getRow() {
		return row;
	}

	public int getRank(int color) {
		return color == WHITE ? ChessNode.LENGTH - row : row + 1;
	}

	public int getColumn() {
		return column;
	}

	public int getFile() {
		return column + 1;
	}

	public Position offset(int color, Position delta) {
		return offset(color, delta.row, delta.column);
	}

	public Position offset(int color, int rowDelta, int columnDelta) {
		return new Position(row + -color * rowDelta, column + columnDelta);
	}

	public boolean inRange() {
		return 0 <= row && row < ChessNode.LENGTH && 0 <= column
				&& column < ChessNode.LENGTH;
	}

	@Override
	public String toString() {
		return (char) (column + 'a') + "" + (ChessNode.LENGTH - row);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Position) {
			Position other = (Position) o;
			return row == other.row && column == other.column;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (row * 31) ^ column;
	}

}
