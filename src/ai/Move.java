package ai;

/**
 * The interface for all moves.
 * 
 * @author arwheelock
 *
 * @param <N>
 */
public interface Move<N extends Node<?, N>> {

	/**
	 * Returns whether or not this move can be legally applied to the given
	 * node.
	 * 
	 * @param node
	 *            - the given node
	 * @return true if the move is legal
	 */
	public boolean isLegal(N node);

	/**
	 * Applies this move to the given node.
	 * 
	 * @param node
	 *            - the given node.
	 */
	public void apply(N node);

}
