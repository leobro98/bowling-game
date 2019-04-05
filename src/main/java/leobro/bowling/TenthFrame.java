package leobro.bowling;

import java.util.List;

/**
 * Represents tenth frame in the game. Contains specific rules for the last frame. Shows the third results slot.
 * Uses {@link TenthFrameResultsFormatter} to output the symbolic representation of its results.
 */
public class TenthFrame extends Frame {

    Integer pins3;
	private TenthFrameResultsFormatter formatter = new TenthFrameResultsFormatter(this);

    @Override
    void registerRoll(int pins) {
        if (isFirstRoll()) {
            pins1 = pins;

            if (isStrike()) {
                containsStrike = true;
            }
        } else if(isSecondRoll()) {
            pins2 = pins;

            if (isSpare()) {
                containsSpare = true;
            } else if(!containsStrike) {
                isComplete = true;
            }
        } else {
            pins3 = pins;
            isComplete = true;
        }

        score += pins;
    }

    private boolean isSecondRoll() {
        return pins2 == null;
    }

    @Override
    boolean isSpare() {
        return !containsStrike && super.isSpare();
    }

	@Override
	public List<String> getDisplaySlots() {
		return formatter.getDisplaySlots();
	}
}
