package chess;

public class SetEnPassant implements SideEffect {

	private Position toSet;

	public SetEnPassant(Position toSet) {
		this.toSet = toSet;
	}

	@Override
	public void apply(ChessNode node) {
		node.setEnPassant(toSet);
	}

}