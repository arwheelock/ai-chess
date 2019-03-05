package ai;

public abstract class Player<M extends Move<N>, N extends Node<M, N>> {

	public static final int WHITE = 1;
	public static final int BLACK = -1;

	protected int color;

	protected Player(int color) {
		this.color = color;
	}

	public abstract M getMove(N node);

	@Override
	public String toString() {
		return color == WHITE ? "White" : "Black";
	}

	public static int colorIndex(int color) {
		return (-color + 1) / 2;
	}

}
