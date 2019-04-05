package leobro.bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Produces the symbolic output of the frame's results thus that the symbols can be directly shown to the player.
 */
class FrameResultsFormatter {

	static final String EMPTY = "";
	static final String STRIKE = "X";

	private Frame frame;

	FrameResultsFormatter(Frame frame) {
		this.frame = frame;
	}

	/**
	 * One or two symbols to show in the player's result table as a result of the frame.
	 *
	 * @return The list of one or two symbols to be displayed in the results table.
	 */
	public List<String> getDisplaySlots() {
		List<String> slots = new ArrayList<>();

		if (frame.containsStrike) {
			slots.add(EMPTY);
			slots.add(STRIKE);
		} else {
			addToSlot(slots, frame.pins1);
			if (frame.containsSpare) {
				slots.add("/");
			} else {
				addToSlot(slots, frame.pins2);
			}
		}
		return slots;
	}

	private static void addToSlot(List<String> slots, Integer pins) {
		if (pins == null) {
			slots.add(EMPTY);
		} else {
			slots.add(pins.toString());
		}
	}
}
