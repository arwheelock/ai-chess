package chess;

import java.util.Scanner;

public class Human extends ChessPlayer {

	private Scanner in;

	public Human(int color) {
		super(color);
		in = new Scanner(System.in);
	}

	@Override
	public ChessMove getMove(ChessNode node) {
		System.out.print(this + ": ");
		String line = in.nextLine().trim();

		if (line.equals("draw")) {
			ChessMove draw = new DrawClaim(color);
			if (draw.isLegal(node)) {
				System.out.println();
				return draw;
			}
		} else if (line.length() >= 4) {
			Position from = new Position(line.substring(0, 2));
			Position to = new Position(line.substring(2, 4));
			Promotion promotion = null;
			if (line.length() > 4) {
				promotion = new Promotion(color, to, charToType(line.charAt(4)));
			}

			if (from.inRange() && node.isAlly(color, from)) {

				for (ChessMove move : node.getLegalMoves()) {
					Promotion movePromotion = move.getPromotion();

					if (move.getFrom().equals(from)
							&& move.getTo().equals(to)
							&& (movePromotion == null ? promotion == null
									: movePromotion.equals(promotion))) {

						System.out.println();
						return move;
					}
				}
			}
		}

		System.out.println("Illegal move, please try again.");
		return (getMove(node));
	}

	private static Type charToType(char c) {
		switch (c) {
		case 'n':
			return new Knight();
		case 'b':
			return new Bishop();
		case 'r':
			return new Rook();
		case 'q':
			return new Queen();
		default:
			return null;
		}
	}

}
