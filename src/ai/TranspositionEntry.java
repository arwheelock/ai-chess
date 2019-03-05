package ai;

public class TranspositionEntry<M extends Move<?>> {

	public static final int LOWER_BOUND = -1;
	public static final int EXACT = 0;
	public static final int UPPER_BOUND = 1;

	private long zobristHash;
	private M bestMove;
	private double value;
	private int depth;
	private int flag;

	public TranspositionEntry(long zobristHash, M bestMove, double value,
			int depth, int flag) {
		this.zobristHash = zobristHash;
		this.bestMove = bestMove;
		this.value = value;
		this.depth = depth;
		this.flag = flag;
	}

	public long getZobristHash() {
		return zobristHash;
	}

	public M getBestMove() {
		return bestMove;
	}

	public void setBestMove(M bestMove) {
		this.bestMove = bestMove;
	}

	public double getValue() {
		return value;
	}

	public int getDepth() {
		return depth;
	}

	public int getFlag() {
		return flag;
	}

}
