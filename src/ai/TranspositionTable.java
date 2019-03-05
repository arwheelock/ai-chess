package ai;

public class TranspositionTable<M extends Move<N>, N extends Node<M, N>> {

	@SuppressWarnings("rawtypes")
	private TranspositionEntry[] table;

	public TranspositionTable(int size) {
		table = new TranspositionEntry[size];
	}

	public void put(N node, TranspositionEntry<M> entry) {
		table[getIndex(node)] = entry;
	}

	@SuppressWarnings("unchecked")
	public TranspositionEntry<M> get(N node) {
		TranspositionEntry<M> entry = table[getIndex(node)];
		if (entry == null || entry.getZobristHash() != node.getZobristHash()) {
			return null;
		}
		return entry;
	}

	private int getIndex(N node) {
		int index = (int) (node.getZobristHash() % table.length);
		if (index < 0) {
			index += table.length;
		}
		return index;
	}
}
