package chess;

import static ai.Player.BLACK;
import static ai.Player.WHITE;
import static ai.Player.colorIndex;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import ai.Player;
import ai.TranspositionPlayer;

public class Chess {

	private ChessNode node;
	private List<Player<ChessMove, ChessNode>> players;

	private Clip clip;

	private Chess() {
		node = new ChessNode();
		players = new Vector<>();
		players.add(new TranspositionPlayer<ChessMove, ChessNode>(WHITE, 1000));
		players.add(new TranspositionPlayer<ChessMove, ChessNode>(BLACK, 1000));
	}

	private void nextPly() {
		System.out.println(this);
		node = node.apply(players.get(colorIndex(node.getColor()))
				.getMove(node));
		playAlert();
	}

	private boolean gameOver() {
		return node.gameOver();
	}

	private void playAlert() {
		if (clip != null) {
			clip.close();
		}

		try (AudioInputStream in = AudioSystem
				.getAudioInputStream(new File("res" + File.separator + "audio"
						+ File.separator + "alert.wav"));) {
			AudioFormat format = in.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(in);
			clip.start();
		} catch (UnsupportedAudioFileException | LineUnavailableException
				| IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return node.toString() + "\n";
	}

	public static void main(String[] args) {
		Chess game = new Chess();
		while (!game.gameOver()) {
			game.nextPly();
		}
		System.out.println(game);
	}

}
