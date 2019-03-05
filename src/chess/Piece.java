package chess;

import static ai.Player.WHITE;

public class Piece {

	private static final char WHITE_PAWN = '\u2659';
	private static final char BLACK_PAWN = '\u265F';

	private Type type;
	private int color;

	public Piece(Type type, int color) {
		this.type = type;
		this.color = color;
	}

	public Piece(int type, int color) {
		switch (type) {
		case Type.PAWN:
			this.type = new Pawn();
			break;
		case Type.KNIGHT:
			this.type = new Knight();
			break;
		case Type.BISHOP:
			this.type = new Bishop();
			break;
		case Type.ROOK:
			this.type = new Rook();
			break;
		case Type.QUEEN:
			this.type = new Queen();
			break;
		case Type.KING:
			this.type = new King();
			break;
		}
		this.color = color;
	}

	public Type getType() {
		return type;
	}

	public int getColor() {
		return color;
	}

	public int getValue() {
		return color * type.getValue();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Piece) {
			Piece other = (Piece) o;
			return color == other.color && type.equals(other.type);
		}
		return false;
	}

	@Override
	public String toString() {
		char pawn = color == WHITE ? WHITE_PAWN : BLACK_PAWN;
		return "" + (char) (pawn - type.getIndex());
	}

}
