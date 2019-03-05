package chess;

import static ai.Player.BLACK;
import static ai.Player.WHITE;
import static ai.Player.colorIndex;
import static chess.Type.BISHOP;
import static chess.Type.BISHOP_VALUE;
import static chess.Type.KING;
import static chess.Type.KNIGHT;
import static chess.Type.PAWN;
import static chess.Type.QUEEN;
import static chess.Type.ROOK;

import java.util.List;
import java.util.Vector;

import ai.Node;

public class ChessNode implements Node<ChessMove, ChessNode> {

	public static final int LENGTH = 8;

	private static final String ANSI_LIGHT = (char) 27 + "[0m";
	private static final String ANSI_DARK = (char) 27 + "[47m";

	private static final int[] setup = new int[] { ROOK, KNIGHT, BISHOP, QUEEN,
			KING, BISHOP, KNIGHT, ROOK };

	private int color;
	private Piece[][] board;
	private boolean[][] canCastle;
	private Position enPassant;

	private List<ChessMove> legalMoves;
	private List<ChessMove> pseudoLegalMoves;
	private boolean foundCheck;
	private boolean check;

	private int value;
	private int material;

	private int fiftyMove;
	private List<ChessNode> history;
	private boolean drawClaimed;

	private long zobristHash;

	public ChessNode() {
		color = WHITE;

		board = new Piece[LENGTH][LENGTH];

		for (int i = 0; i < board.length; i++) {
			set(new Position(0, i), new Piece(setup[i], BLACK));
			set(new Position(1, i), new Piece(PAWN, BLACK));
			set(new Position(LENGTH - 2, i), new Piece(PAWN, WHITE));
			set(new Position(LENGTH - 1, i), new Piece(setup[i], WHITE));
		}

		canCastle = new boolean[][] { { true, true }, { true, true } };

		history = new Vector<>();
	}

	private ChessNode(ChessNode toCopy) {
		color = toCopy.color;

		board = new Piece[LENGTH][LENGTH];
		for (int i = 0; i < board.length; i++) {
			System.arraycopy(toCopy.board[i], 0, board[i], 0, board[0].length);
		}

		canCastle = new boolean[][] {
				{ toCopy.canCastle[0][0], toCopy.canCastle[0][1] },
				{ toCopy.canCastle[1][0], toCopy.canCastle[1][1] } };
		enPassant = toCopy.enPassant;

		value = toCopy.value;
		material = toCopy.material;

		fiftyMove = toCopy.fiftyMove;
		history = new Vector<>(toCopy.history);
		drawClaimed = toCopy.drawClaimed;

		zobristHash = toCopy.zobristHash;
	}

	// TODO: check order here
	@Override
	public ChessNode apply(ChessMove move) {
		ChessNode copy = new ChessNode(this);
		copy.history.add(this);
		copy.applyHere(move);
		return copy;
	}

	// TODO: check order here
	private void applyHere(ChessMove move) {
		resetEnPassant();
		fiftyMove++;
		move.apply(this);
		color *= -1;
		zobristHash ^= ZobristTable.color();
	}

	@Override
	public List<ChessMove> getLegalMoves() {
		if (legalMoves != null) {
			return legalMoves;
		}

		legalMoves = new Vector<>();
		for (ChessMove move : getPseudoLegalMoves()) {
			if (move.isLegal(this)) {
				legalMoves.add(move);
			}
		}
		ChessMove draw = new DrawClaim(color);
		if (draw.isLegal(this)) {
			legalMoves.add(draw);
		}

		return legalMoves;
	}

	public List<ChessMove> getPseudoLegalMoves() {
		if (pseudoLegalMoves != null) {
			return pseudoLegalMoves;
		}
		pseudoLegalMoves = new Vector<>();

		for (int i = 0; i < ChessNode.LENGTH; i++) {
			for (int j = 0; j < ChessNode.LENGTH; j++) {
				Position pos = new Position(i, j);

				if (!isEmpty(pos)) {
					Piece piece = get(pos);
					int color = piece.getColor();

					if (color == this.color) {
						pseudoLegalMoves.addAll(MoveTable.get(color,
								piece.getType(), pos).getPseudoLegal(this));
					}
				}
			}
		}
		return pseudoLegalMoves;
	}

	public void set(Position pos, Piece piece) {
		remove(pos);
		value += piece.getValue();
		value += PieceSquareTable.getValue(piece, pos, this);
		material += Math.abs(piece.getValue());
		zobristHash ^= ZobristTable.get(pos, piece);
		board[pos.getRow()][pos.getColumn()] = piece;
	}

	public void move(Position from, Position to) {
		set(to, get(from));
		remove(from);
	}

	public void remove(Position pos) {
		if (!isEmpty(pos)) {
			Piece piece = get(pos);
			value -= piece.getValue();
			value -= PieceSquareTable.getValue(piece, pos, this);
			material -= Math.abs(piece.getValue());
			zobristHash ^= ZobristTable.get(pos, piece);
		}
		board[pos.getRow()][pos.getColumn()] = null;
	}

	public int getColor() {
		return color;
	}

	public boolean isEmpty(Position pos) {
		return board[pos.getRow()][pos.getColumn()] == null;
	}

	public Piece get(Position pos) {
		return board[pos.getRow()][pos.getColumn()];
	}

	public boolean isAlly(int color, Position pos) {
		return !isEmpty(pos) && color == get(pos).getColor();
	}

	public boolean isEnemy(int color, Position pos) {
		return !isEmpty(pos) && color != get(pos).getColor();
	}

	public void prohibitCastling(int color, int side) {
		if (canCastle(color, side)) {
			zobristHash ^= ZobristTable.canCastle(color, side);
			canCastle[colorIndex(color)][Castling.castlingIndex(side)] = false;
		}
	}

	public boolean canCastle(int color, int side) {
		return canCastle[colorIndex(color)][Castling.castlingIndex(side)];
	}

	public void setEnPassant(Position pos) {
		zobristHash ^= ZobristTable.enPassant(pos.getColumn());
		enPassant = pos;
	}

	public void resetEnPassant() {
		if (canEnPassant()) {
			zobristHash ^= ZobristTable.enPassant(getEnPassant().getColumn());
			enPassant = null;
		}
	}

	public boolean canEnPassant() {
		return enPassant != null;
	}

	public Position getEnPassant() {
		return enPassant;
	}

	@Override
	public boolean gameOver() {
		return drawClaimed || inCheckmate() || inStalemate()
				|| insufficientMaterial();
	}

	public boolean inCheckmate() {
		return inCheck() && getLegalMoves().isEmpty();
	}

	public boolean inStalemate() {
		return !inCheck() && getLegalMoves().isEmpty();
	}

	public boolean inCheck() {
		if (foundCheck) {
			return check;
		}
		check = !(new NullMove(color).isLegal(this));
		foundCheck = true;
		return check;
	}

	public boolean insufficientMaterial() {
		int zeroed = this.material - Type.KING_VALUE * 2;

		if (zeroed == 0 || zeroed == Type.KNIGHT_VALUE
				|| zeroed == Type.BISHOP_VALUE) {
			return true;
		} else if (zeroed % BISHOP_VALUE == 0) {
			int first = -1;

			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					Position pos = new Position(i, j);

					if (!isEmpty(pos)) {
						Type type = get(pos).getType();
						if (type.equals(new Bishop())) {
							if (first == -1) {
								first = (i + j) % 2;
							} else {
								if (first != (i + j) % 2) {
									return false;
								}
							}
						} else if (!type.equals(new King())) {
							return false;
						}
					}
				}
			}

			return true;
		}

		return false;
	}

	// TODO: Improve this
	public boolean inEndgame() {
		return material - 2 * Type.KING_VALUE <= 26 * Type.PAWN_VALUE;
	}

	public int getFiftyMoveCount() {
		return fiftyMove;
	}

	public void resetFiftyMoveCount() {
		fiftyMove = 0;
	}

	public List<ChessNode> getHistory() {
		return history;
	}

	public void claimDraw() {
		drawClaimed = true;
	}

	@Override
	public int getEstimate() {
		if (drawClaimed) {
			return 0;
		}
		return value;
	}

	@Override
	public double getValue(int color) {
		if (inCheckmate()) {
			return color == WHITE ? BLACK_WIN : WHITE_WIN;
		} else if (drawClaimed || inStalemate()) {
			return 0;
		} else {
			return value;
		}
	}

	@Override
	public long getZobristHash() {
		return zobristHash;
	}

	@Override
	public int hashCode() {
		return new Long(zobristHash).hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ChessNode)) {
			return false;
		}
		ChessNode other = (ChessNode) o;

		if (zobristHash != other.zobristHash) {
			return false;
		}

		if (color != other.color) {
			return false;
		}

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] == null) {
					if (other.board[i][j] != null) {
						return false;
					}
				} else if (!board[i][j].equals(other.board[i][j])) {
					return false;
				}
			}
		}

		for (int i = 0; i < canCastle.length; i++) {
			for (int j = 0; j < canCastle[0].length; j++) {
				if (canCastle[i][j] != other.canCastle[i][j]) {
					return false;
				}
			}
		}

		if (enPassant == null) {
			if (other.enPassant != null) {
				return false;
			}
		} else if (!enPassant.equals(other.enPassant)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("  ");
		for (char c = 'a'; c <= 'h'; c++) {
			builder.append(c).append(" ");
		}
		builder.append("\n");

		boolean dark = false;
		for (int i = 0; i < ChessNode.LENGTH; i++) {
			Piece[] row = board[i];

			builder.append(ChessNode.LENGTH - i).append(" ");

			for (Piece p : row) {
				if (dark) {
					builder.append(ANSI_DARK);
				}
				builder.append(p == null ? " " : p).append(" ")
						.append(ANSI_LIGHT);
				dark = !dark;
			}

			builder.append(" ").append(ChessNode.LENGTH - i).append("\n");
			dark = !dark;
		}

		builder.append("  ");
		for (char c = 'a'; c <= 'h'; c++) {
			builder.append(c).append(" ");
		}

		return builder.toString();
	}

}
