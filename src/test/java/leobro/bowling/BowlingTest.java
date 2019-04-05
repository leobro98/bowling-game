package leobro.bowling;

import org.junit.Before;
import org.junit.Test;

import java.security.InvalidParameterException;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class BowlingTest {

    private List<String> playerNames;
    private Game twoPlayerGame;
    private Game onePlayerGame;

    @Before
    public void setUp() {
        playerNames = new ArrayList<>();
        playerNames.add("First Player");
        onePlayerGame = new Game(playerNames);
        playerNames.add("Second");
        twoPlayerGame = new Game(playerNames);
    }

	private Map<Integer, String> getPlayersMap() {
		Map<Integer, String> players = new HashMap<>();

		for (int i = 0; i < playerNames.size(); i++) {
			players.put(i, playerNames.get(i));
		}
		return players;
	}

    private static void throwSeries(Game game, int... series) {
        for (int pins : series) {
            game.roll(pins);
        }
    }

    private static void playNineGutterFrames(Game game) {
        for (int i = 0; i < 9; i++) {
            for (int playerIndex : game.getPlayers().keySet()) {
                game.roll(0);
                game.roll(0);
            }
        }
    }

    @Test
    public void canListPlayers() {
		Map<Integer, String> players = getPlayersMap();

		assertThat(twoPlayerGame.getPlayers(), is(equalTo(players)));
    }

	@Test
    public void canCalculateScoreForGutterFrame() {
        onePlayerGame.roll(0);
		onePlayerGame.roll(0);

        assertEquals(0, onePlayerGame.getPlayerScore(0));
    }

    @Test
	public void canCalculateScoreForNotFinishedFrame() {
		onePlayerGame.roll(1);

		assertEquals(1, onePlayerGame.getPlayerScore(0));
	}

    @Test
    public void canCalculateScoreForOpenFrame() {
        onePlayerGame.roll(1);
		onePlayerGame.roll(4);

        assertEquals(5, onePlayerGame.getPlayerScore(0));
    }

    @Test
    public void canCalculateScoreForSeveralOpenFrames() {
        throwSeries(onePlayerGame, 1, 4, 4, 5);

        assertEquals(14, onePlayerGame.getPlayerScore(0));
    }

    @Test
    public void canCalculateScoreForSpare() {
        throwSeries(onePlayerGame, 1, 4, 6, 4);
        assertEquals(15, onePlayerGame.getPlayerScore(0));

        onePlayerGame.roll(5);
        assertEquals(25, onePlayerGame.getPlayerScore(0));
    }

    @Test
    public void canCalculateScoreForStrike() {
        throwSeries(onePlayerGame, 1, 4, 10);
        assertEquals(15, onePlayerGame.getPlayerScore(0));

        throwSeries(onePlayerGame, 1, 3);
        assertEquals(23, onePlayerGame.getPlayerScore(0));
    }

    @Test
    public void canCalculateScoreForTenthFrame() {
        throwSeries(onePlayerGame, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 8, 6);

        assertEquals(16, onePlayerGame.getPlayerScore(0));
    }

    @Test
    public void canDetermineNextPlayerForGutterFrame() {
        twoPlayerGame.roll(0);
        assertEquals(0, twoPlayerGame.getNextPlayer());

        twoPlayerGame.roll(0);
        assertEquals(1, twoPlayerGame.getNextPlayer());
    }

    @Test
    public void canDetermineNextPlayerForOpenFrame() {
        twoPlayerGame.roll(1);
        assertEquals(0, twoPlayerGame.getNextPlayer());

        twoPlayerGame.roll(4);
        assertEquals(1, twoPlayerGame.getNextPlayer());
    }

    @Test
    public void canDetermineNextPlayerForSpare() {
        throwSeries(twoPlayerGame, 1, 4, 0, 0, 6, 4);

        assertEquals(1, twoPlayerGame.getNextPlayer());
    }

    @Test
    public void canDetermineNextPlayerForStrike() {
        throwSeries(twoPlayerGame, 1, 4, 0, 0, 10);

        assertEquals(1, twoPlayerGame.getNextPlayer());
    }

    @Test
    public void canDetermineNextPlayerForTenthFameSpare() {
        throwSeries(twoPlayerGame,
				10, 0, 0,
				10, 0, 0,
				10, 0, 0,
				10, 0, 0,
				10, 0, 0,
				10, 0, 0,
				10, 0, 0,
				10, 0, 0,
				10, 0, 0,
				6, 4);

        assertEquals(0, twoPlayerGame.getNextPlayer());
    }

    @Test
    public void canShowFirstPlayerTable() {
        throwSeries(twoPlayerGame,
				1, 4, 3, 5,
				6, 4, 3, 5,
				10);
		Frame[] frames = twoPlayerGame.getPlayerResultsTable(0);

		Frame frame = frames[0];
		assertThat(frame.getDisplaySlots(), is(equalTo(Arrays.asList("1", "4"))));
        assertEquals(5, frame.getScore());
        assertEquals(5, frame.getAccumulatedScore());

		frame = frames[1];
        assertThat(frame.getDisplaySlots(), is(equalTo(Arrays.asList("6", "/"))));
        assertEquals(20, frame.getScore());
        assertEquals(25, frame.getAccumulatedScore());

		frame = frames[2];
        assertThat(frame.getDisplaySlots(), is(equalTo(Arrays.asList("", "X"))));
        assertEquals(10, frame.getScore());
        assertEquals(35, frame.getAccumulatedScore());
    }

    @Test
    public void canShowPlayerTableWithTenthFrame() {
        playNineGutterFrames(onePlayerGame);
        throwSeries(onePlayerGame, 2, 8, 6);
		Frame[] frames = onePlayerGame.getPlayerResultsTable(0);
		Frame frame = frames[9];

		assertThat(frame.getDisplaySlots(), is(equalTo(Arrays.asList("2", "/", "6"))));
        assertEquals(16, frame.getScore());
        assertEquals(16, frame.getAccumulatedScore());
    }

    @Test
    public void canShowPlayerTableWithTenthFrameStrikes() {
        playNineGutterFrames(onePlayerGame);
        throwSeries(onePlayerGame, 10, 10, 10);
		Frame[] frames = onePlayerGame.getPlayerResultsTable(0);
		Frame frame = frames[9];

		assertThat(frame.getDisplaySlots(), is(equalTo(Arrays.asList("X", "X", "X"))));
        assertEquals(30, frame.getScore());
        assertEquals(30, frame.getAccumulatedScore());
    }

    @Test
    public void canShowWinner() {
        playNineGutterFrames(twoPlayerGame);
        throwSeries(twoPlayerGame, 0, 0, 2, 8, 6);

		assertFalse(twoPlayerGame.isGameActive());
        assertEquals(1, twoPlayerGame.getWinner());
    }

    @Test(expected = InvalidParameterException.class)
    public void negativeRollsAreNotAccepted() {
        onePlayerGame.roll(-1);
    }

    @Test(expected = InvalidParameterException.class)
    public void moreThanTenPinsInOneFrameAreNotAccepted() {
        onePlayerGame.roll(6);
        onePlayerGame.roll(5);
    }
}
