package ai;

import java.util.List;

public interface Node<M extends Move<N>, N extends Node<M, N>> {

	public static final double WHITE_WIN = Player.WHITE * 1E10;
	public static final double BLACK_WIN = Player.BLACK * 1E10;

	public N apply(M move);

	public List<M> getLegalMoves();

	public boolean gameOver();

	public int getEstimate();

	public double getValue(int color);

	public long getZobristHash();

}
