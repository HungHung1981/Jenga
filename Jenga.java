import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Jenga {

	private static final String CREDIT =
			"Jenga with code by Thomas Meijers, Jonas van Oenen, Nina läuger, Alessandra van Ree. For Sofia";
	private static final Random RANDOM = new Random();

	public static void main(String[] args) {
		System.out.println(CREDIT);
		printTitle();

		Scanner input = new Scanner(System.in);
		Integer playerCount = readNumber(input, "How many players? ", 2, 20);
		if (playerCount == null) {
			return;
		}

		Integer computerCount = readNumber(input, "How many computer players? ", 0, playerCount - 1);
		if (computerCount == null) {
			return;
		}

		List<Player> players = createPlayers(playerCount, computerCount);
		List<GameLayer> tower = createTower();
		int currentPlayer = 0;

		System.out.println();
		System.out.println("Players: " + describePlayers(players));
		System.out.println("Remove a block by entering its layer and column.");
		System.out.println("The top two layers are off limits. The next player loses if no legal move remains.");
		System.out.println("If the tower's center of mass moves beyond its supports, it collapses.");

		while (true) {
			printTower(tower);
			List<BlockPosition> legalMoves = findLegalMoves(tower);
			Player player = players.get(currentPlayer);

			if (legalMoves.isEmpty()) {
				Player winner = players.get((currentPlayer + players.size() - 1) % players.size());
				System.out.println(player.name + " has no legal move. " + winner.name + " wins!");
				break;
			}

			System.out.println();
			System.out.println(player.name + "'s turn.");
			BlockPosition move;
			if (player.computer) {
				move = legalMoves.get(RANDOM.nextInt(legalMoves.size()));
				System.out.println("The computer removes layer " + (move.layer + 1)
						+ ", column " + (move.column + 1) + ".");
			} else {
				move = readMove(input, tower, legalMoves);
				if (move == null) {
					System.out.println("Game ended.");
					return;
				}
			}

			tower.get(move.layer).blocks[move.column] = false;
			addBlockOnTop(tower);
			if (!isStable(tower)) {
				printTower(tower);
				System.out.println();
				System.out.println("!!! THE TOWER HAS FALLEN !!!");
				Player winner = players.get((currentPlayer + 1) % players.size());
				System.out.println(player.name + " caused the collapse. " + winner.name + " wins!");
				break;
			}
			currentPlayer = (currentPlayer + 1) % players.size();
		}
	}

	private static void printTitle() {
		System.out.println();
		System.out.println("       __      __");
		System.out.println("      / /___  / /____  ____ _____");
		System.out.println(" __  / / __ \\/ __/ _ \\/ __ `/ __ \\");
		System.out.println("/ /_/ / /_/ / /_/  __/ /_/ / /_/ /");
		System.out.println("\\____/\\____/\\__/\\___/\\__,_/\\____/");
		System.out.println("                 |  J E N G A  |");
		System.out.println();
	}

	private static Integer readNumber(Scanner input, String prompt, int minimum, int maximum) {
		while (true) {
			System.out.print(prompt);
			if (!input.hasNextLine()) {
				return null;
			}

			String answer = input.nextLine().trim();
			try {
				int value = Integer.parseInt(answer);
				if (value >= minimum && value <= maximum) {
					return value;
				}
			} catch (NumberFormatException ignored) {
				// The prompt below explains the accepted range.
			}
			System.out.println("Please enter a whole number from " + minimum + " to " + maximum + ".");
		}
	}

	private static List<Player> createPlayers(int playerCount, int computerCount) {
		List<Player> players = new ArrayList<Player>();
		int humanCount = playerCount - computerCount;
		for (int i = 1; i <= humanCount; i++) {
			players.add(new Player("Player " + i, false));
		}
		for (int i = 1; i <= computerCount; i++) {
			players.add(new Player("Computer " + i, true));
		}
		return players;
	}

	private static String describePlayers(List<Player> players) {
		StringBuilder description = new StringBuilder();
		for (Player player : players) {
			if (description.length() > 0) {
				description.append(", ");
			}
			description.append(player.name);
		}
		return description.toString();
	}

	private static List<GameLayer> createTower() {
		List<GameLayer> tower = new ArrayList<GameLayer>();
		for (int i = 0; i < 18; i++) {
			tower.add(new GameLayer(i % 2 == 0 ? 'X' : 'Y', true));
		}
		return tower;
	}

	private static void addBlockOnTop(List<GameLayer> tower) {
		GameLayer top = tower.get(tower.size() - 1);
		for (int column = 0; column < 3; column++) {
			if (!top.blocks[column]) {
				top.blocks[column] = true;
				return;
			}
		}

		char nextDirection = top.direction == 'X' ? 'Y' : 'X';
		GameLayer newTop = new GameLayer(nextDirection, false);
		newTop.blocks[0] = true;
		tower.add(newTop);
	}

	private static boolean isStable(List<GameLayer> tower) {
		for (int supportLayer = 1; supportLayer < tower.size() - 1; supportLayer++) {
			GameLayer support = tower.get(supportLayer);
			int minimumColumn = 3;
			int maximumColumn = -1;
			for (int column = 0; column < 3; column++) {
				if (support.blocks[column]) {
					minimumColumn = Math.min(minimumColumn, column);
					maximumColumn = Math.max(maximumColumn, column);
				}
			}
			if (maximumColumn < 0) {
				return false;
			}

			double centerX = 0;
			double centerY = 0;
			int blockCount = 0;
			for (int layer = supportLayer + 1; layer < tower.size(); layer++) {
				GameLayer current = tower.get(layer);
				for (int column = 0; column < 3; column++) {
					if (!current.blocks[column]) {
						continue;
					}
					if (current.direction == 'X') {
						centerY += column - 1;
					} else {
						centerX += column - 1;
					}
					blockCount++;
				}
			}
			if (blockCount == 0) {
				return false;
			}
			centerX /= blockCount;
			centerY /= blockCount;

			double minimumSupport = minimumColumn - 1.5;
			double maximumSupport = maximumColumn - 0.5;
			if (support.direction == 'X') {
				if (centerY < minimumSupport || centerY > maximumSupport) {
					return false;
				}
			} else if (centerX < minimumSupport || centerX > maximumSupport) {
				return false;
			}
		}
		return true;
	}

	private static List<BlockPosition> findLegalMoves(List<GameLayer> tower) {
		List<BlockPosition> moves = new ArrayList<BlockPosition>();
		int highestRemovableLayer = tower.size() - 2;
		for (int layer = 0; layer < highestRemovableLayer; layer++) {
			for (int column = 0; column < 3; column++) {
				if (tower.get(layer).blocks[column]) {
					moves.add(new BlockPosition(layer, column));
				}
			}
		}
		return moves;
	}

	private static BlockPosition readMove(Scanner input, List<GameLayer> tower,
			List<BlockPosition> legalMoves) {
		while (true) {
			Integer layerNumber = readNumber(input, "Layer (1-" + (tower.size() - 2) + "): ",
					1, tower.size() - 2);
			if (layerNumber == null) {
				return null;
			}
			Integer columnNumber = readNumber(input, "Column (1-3): ", 1, 3);
			if (columnNumber == null) {
				return null;
			}

			BlockPosition requested = new BlockPosition(layerNumber - 1, columnNumber - 1);
			if (legalMoves.contains(requested)) {
				return requested;
			}
			System.out.println("That block is empty or cannot be removed. Please choose another.");
		}
	}

	private static void printTower(List<GameLayer> tower) {
		int labelWidth = Integer.toString(tower.size()).length();
		System.out.println();
		System.out.println("                         /\\");
		for (int layer = tower.size() - 1; layer >= 0; layer--) {
			GameLayer current = tower.get(layer);
			StringBuilder row = new StringBuilder();
			row.append(String.format("%" + labelWidth + "d |", layer + 1));
			for (boolean present : current.blocks) {
				row.append(present ? "[====]" : "[    ]");
			}
			row.append("| ").append(current.direction);
			System.out.println(row);
		}
		System.out.println("   +------------------+");
		System.out.println("    1     2     3       X/Y = block direction");
	}

	private static final class GameLayer {
		private final char direction;
		private final boolean[] blocks = new boolean[3];

		private GameLayer(char direction, boolean full) {
			this.direction = direction;
			if (full) {
				blocks[0] = true;
				blocks[1] = true;
				blocks[2] = true;
			}
		}
	}

	private static final class BlockPosition {
		private final int layer;
		private final int column;

		private BlockPosition(int layer, int column) {
			this.layer = layer;
			this.column = column;
		}

		@Override
		public boolean equals(Object other) {
			if (!(other instanceof BlockPosition)) {
				return false;
			}
			BlockPosition position = (BlockPosition) other;
			return layer == position.layer && column == position.column;
		}

		@Override
		public int hashCode() {
			return 31 * layer + column;
		}
	}

	private static final class Player {
		private final String name;
		private final boolean computer;

		private Player(String name, boolean computer) {
			this.name = name;
			this.computer = computer;
		}
	}
}
