package leobro.bowling;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * Frame for one player. Knows part of the game rules concerning playing a frame.
 * Uses {@link FrameResultsFormatter} to output the symbolic representation of its results.
 */
public class Frame {

	static final int PINS_IN_FRAME = 10;

	Integer pins1;
    Integer pins2;
    boolean isComplete;
    int score;
    private int accumulatedScore;
    boolean containsSpare;
    boolean containsStrike;

	private int pendingRolls;
	private FrameResultsFormatter formatter = new FrameResultsFormatter(this);

    /**
     * Individual score of the frame. If the frame contains spare or strike, the score might be updated as a result
	 * of following rolls.
	 *
     * @return score of the frame.
     */
    public int getScore() {
        return score;
    }

    /**
     * Shows if there were a spare in the frame.
	 *
     * @return {@literal true} if the frame contains spare; otherwise, {@literal false}.
     */
    public boolean containsSpare() {
        return containsSpare;
    }

    /**
     * Shows if there were a strike in the frame.
	 *
     * @return {@literal true} if the frame contains strike; otherwise, {@literal false}.
     */
    public boolean containsStrike() {
        return containsStrike;
    }

    /**
     * Returns the current score of the player for the moment of the frame.
	 *
     * @return total score of the player for the given frame.
     */
    public int getAccumulatedScore() {
        return accumulatedScore;
    }

    void setAccumulatedScore(int score) {
        accumulatedScore = score;
    }

    void incrementScore(int pins) {
        score += pins;
    }

    boolean hasPendingRolls() {
        return pendingRolls > 0;
    }

    void decrementPendingRolls() {
        pendingRolls--;
    }

    boolean isComplete() {
        return isComplete;
    }

    void registerRoll(int pins) {
        assertPinsArePositive(pins);
        assertPinsLessOrEqualToMaximum(pins);

        if (isFirstRoll()) {
			pins1 = pins;

            if (isStrike()) {
                containsStrike = true;
                pendingRolls = 2;
                isComplete = true;
            }
        } else {
            assertTotalPinsLessOrEqualToMaximum(pins);
			pins2 = pins;

            if (isSpare()) {
                containsSpare = true;
                pendingRolls = 1;
            }
            isComplete = true;
        }

        score += pins;
    }

    boolean isFirstRoll() {
        return pins1 == null;
    }

    boolean isStrike() {
        return pins1 == PINS_IN_FRAME;
    }

    boolean isSpare() {
        return pins1 + pins2 == PINS_IN_FRAME;
    }

    private void assertPinsArePositive(int pins) {
        if (pins < 0) {
            throw new InvalidParameterException("Negative pins are not accepted");
        }
    }

    private void assertPinsLessOrEqualToMaximum(int pins) {
        if (pins > PINS_IN_FRAME) {
            throw new InvalidParameterException("Too many pins");
        }
    }

    private void assertTotalPinsLessOrEqualToMaximum(int pins) {
        if (pins1 + pins > PINS_IN_FRAME) {
            throw new InvalidParameterException("Too many pins in the frame");
        }
    }

	/**
	 * One or two symbols to show in the player's result table as a result of the frame.
	 *
	 * @return The list of one or two symbols to be displayed in the results table.
	 */
	public List<String> getDisplaySlots() {
		return formatter.getDisplaySlots();
	}
}
