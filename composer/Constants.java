package composer;

public final class Constants {
    final static public int BPM = 60;
    final static public int VOLUME = 60;
    final static public int TICKS_PER_SECOND = 100;
    final static public int TICKS_PER_BEAT = (60 / BPM) * TICKS_PER_SECOND; //calculation that will be accurate even if we change BPM and ticksPerSecond
    final static public int TICKS_PER_PIXEL = 1;
    final static public int LINE_WIDTH = 1;
    final static public int NUM_BARS = 127;
    final static public int BAR_DISTANCE = 10;
    final static public int NOTE_INPUT_PANE_WIDTH = 1280;
    final static public int NOTE_INPUT_PANE_HEIGHT = 2000;
    final static public int RECT_WIDTH = 100;

    public static int calculatePitch(int yCord){
        return (int) Math.floor((Constants.NOTE_INPUT_PANE_WIDTH - yCord)/Constants.BAR_DISTANCE);
    }

    public static int calculateStartTick(int xCord){
        return xCord* Constants.TICKS_PER_PIXEL;
    }

    public static int calculateEndTick(int xCord){
        return (xCord + Constants.RECT_WIDTH)* Constants.TICKS_PER_PIXEL;
    }

}
