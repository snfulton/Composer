package composer.BooleanChecker;

import composer.Constants;
import composer.MusicRectangle.MusicRectangle;

public class WouldBeTooSmall implements BooleanChecker{
    private int changeInX;
    public WouldBeTooSmall(int changeInX){
        this.changeInX = changeInX;
    }

    @Override
    public boolean check(MusicRectangle musicRectangle){
        int hypotheticalLength = musicRectangle.getDurationTick()+(changeInX* Constants.TICKS_PER_PIXEL);
        boolean isTooSmall = hypotheticalLength < 5;
        return isTooSmall;
    }
}
