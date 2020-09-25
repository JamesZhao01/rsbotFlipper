package gui.mouse;

import java.awt.*;

/**
 * Credits to Shaft: https://osbot.org/forum/topic/143732-mouse-trail-and-cursor-classes/
 */
public class MousePathPoint extends Point {

    private long finishTime;


    public MousePathPoint(int x, int y, int lastingTime) {
        super(x, y);
        finishTime = System.currentTimeMillis() + lastingTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MousePathPoint) {
            MousePathPoint pt = (MousePathPoint) obj;
            return (x == pt.x) && (y == pt.y);
        }
        return super.equals(obj);
    }

    public boolean isUp() {
        return System.currentTimeMillis() > finishTime;
    }
}