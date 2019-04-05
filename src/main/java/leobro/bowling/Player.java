package leobro.bowling;

/**
 * Player in the bowling game. Index is their sequential index in the list of players, and the name is needed for display.
 * Knows the rules of the game (passes part of the rules evaluation to {@link Frame}), calculates the score and fills
 * the frames. Knows the current score and whether there is one more roll in the current frame.
 */
class Player {

    private int index;
    private String name;
    private int currentScore;
    private Frame[] frames;
    private int currentFrameIndex;

    Player(int sequenceNumber, String name) {
        index = sequenceNumber;
        this.name = name;
		currentScore = 0;
		initializeFrames();
    }

    private void initializeFrames() {
        frames = new Frame[10];
        for (int i = 0; i < 9; i++) {
            frames[i] = new Frame();
        }
        frames[9] = new TenthFrame();

		currentFrameIndex = 0;
    }

    int getCurrentScore() {
        return currentScore;
    }

    int getIndex() {
        return index;
    }

    String getName() {
        return name;
    }

    /**
     * All game rules are in this method and in the downstream methods.
     */
    void roll(int pins) {
        Frame frame = frames[currentFrameIndex];
        frame.registerRoll(pins);
        fillPendingScore(pins);
        calculateCurrentScore();
        if (frame.isComplete()) {
            currentFrameIndex++;
        }
    }

    /**
     * Adds score from one or two next rolls to the waiting spare or strike frame.
     */
    private void fillPendingScore(int pins) {
        for (int i = 0; i < currentFrameIndex; i++) {
            Frame frame = frames[i];

            if (frame.hasPendingRolls()) {
                frame.incrementScore(pins);
                frame.decrementPendingRolls();
            }
        }
    }

    private void calculateCurrentScore() {
        currentScore = 0;

		for (Frame frame : frames) {
			currentScore += frame.getScore();
			frame.setAccumulatedScore(currentScore);
		}
    }

    /**
     * Determines whether the player has completed their rolls in the current frame and the next player obtains a bowl.
     */
    boolean isFrameComplete(int frameIndex) {
        return frames[frameIndex].isComplete();
    }

    public Frame[] getResultsTable() {
        return frames;
    }
}
