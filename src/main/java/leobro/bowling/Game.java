package leobro.bowling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entry point of the component. Represents a whole bowling game for several players.
 * Responsible for handling of players and passing a control from one player to another.
 */
public class Game {

    private List<Player> players;
    private Player currentPlayer;
    private int currentFrame;
    private boolean isGameActive;

    /**
     * Creates a new bowling game. The list of players can not be changed later. The given order of players will remain
     * the same till the end of the game.
	 *
     * @param playerNames The names of players as they will be shown in the game table.
     */
    public Game(List<String> playerNames) {
        isGameActive = true;
		players = new ArrayList<>();

        for (int i = 0; i < playerNames.size(); i++) {
            Player player = new Player(i, playerNames.get(i));
            players.add(i, player);
        }

		currentFrame = 0;
        activateFirstPlayer();
    }

    /**
     * Returns the players in this game. Key in the Map is the index of a player in the list starting from zero.
	 *
     * @return The Map where the key in the index of a player, and value is the name of the player.
     */
    public Map<Integer, String> getPlayers() {
        Map<Integer, String> names = new HashMap<>();

        for (Player player : players) {
            names.put(player.getIndex(), player.getName());
        }
        return names;
    }

    /**
     * Returns the index for the player in the list of players, who will roll the next bowl (not yet registered).
	 *
     * @return The zero-bound index of the next player.
     */
    public int getNextPlayer() {
        return currentPlayer.getIndex();
    }

    /**
     * Returns the current score of the player with given index.
	 *
     * @param playerIndex The index of the player.
     * @return The current score for the player.
     */
    public int getPlayerScore(int playerIndex) {
        return players.get(playerIndex).getCurrentScore();
    }

    /**
     * Returns the full table of results for the player with given index.
	 *
     * @param playerIndex The index of the player.
     * @return The results table for the player where {@link Frame#getDisplaySlots() display slots} contain symbols
     * ready for display according to bowling rules.
     */
    public Frame[] getPlayerResultsTable(int playerIndex) {
        return players.get(playerIndex).getResultsTable();
    }

    /**
     * Returns the current frame index (zero-bound).
	 *
     * @return the index of the frame into which the result of the next roll will be registered.
     */
    public int getCurrentFrame() {
        return currentFrame;
    }

    /**
     * Shows if the game still continues or is over.
	 *
     * @return {@literal true} if the game is going; {@literal false} if the game is over.
     */
    public boolean isGameActive() {
        return isGameActive;
    }

    /**
     * Return the index of the winner of the game.
	 *
     * @return the zero-bound index of the game winner in the list of players, if the game is over;
     * otherwise {@literal -1}.
     */
    public int getWinner() {
        if (isGameActive) {
            return -1;
        }

        int winner = 0;
        int maxScore = 0;

        for (Player player : players) {
            if (player.getCurrentScore() > maxScore) {
                winner = player.getIndex();
                maxScore = player.getCurrentScore();
            }
        }
        return winner;
    }

    /**
     * Used to register the result of rolling of the current bowl. The game determines itself which player rolled
	 * the bowl and which player will throw the next.
	 *
     * @param pins The count of pins hit by this throw.
     */
    public void roll(int pins) {
        if (isGameActive) {
            currentPlayer.roll(pins);

            if (currentPlayer.isFrameComplete(currentFrame)) {
                activateNextPlayer();
            }
        }
    }

    private void activateNextPlayer() {
        if (isTheLastPlayer()) {
            if (!isTheLastFrame()) {
                currentFrame++;
                activateFirstPlayer();
            } else {
                setGameOver();
            }
        } else {
            activateNextPlayerInTheRow();
        }
    }

    private boolean isTheLastFrame() {
        return currentFrame == 9;
    }

    private boolean isTheLastPlayer() {
        return currentPlayer.getIndex() == players.size() - 1;
    }

    private void activateFirstPlayer() {
        currentPlayer = players.get(0);
    }

    private void activateNextPlayerInTheRow() {
        currentPlayer = players.get(currentPlayer.getIndex() + 1);
    }

    private void setGameOver() {
        isGameActive = false;
    }
}
