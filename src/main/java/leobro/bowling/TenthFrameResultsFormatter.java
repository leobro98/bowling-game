package leobro.bowling;

import java.util.ArrayList;
import java.util.List;

import static leobro.bowling.Frame.PINS_IN_FRAME;

/**
 * Produces the symbolic output of the frame's results specific to the tenth frame.
 */
public class TenthFrameResultsFormatter extends FrameResultsFormatter {

	private TenthFrame frame;

	TenthFrameResultsFormatter(Frame frame) {
		super(frame);
		this.frame = (TenthFrame) frame;
	}

	@Override
	public List<String> getDisplaySlots() {
		List<String> slots = new ArrayList<>();

		if (frame.containsStrike) {
			slots.add(STRIKE);

			if (frame.pins2 == PINS_IN_FRAME) {
				slots.add(STRIKE);
			} else {
				slots.add(frame.pins2.toString());
			}
		} else {
			slots = super.getDisplaySlots();
		}

		if (frame.pins3 != null) {
			if (frame.pins3 == PINS_IN_FRAME) {
				slots.add(STRIKE);
			} else {
				slots.add(frame.pins3.toString());
			}
		}

		return slots;
	}
}
