import com.sun.corba.se.impl.oa.toa.TOA;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Random;

/**
 * Created by kevin on 1/17/17.
 */
public class Mouse{
    private volatile int SLEEP_MS;
    private final int TOLERANCE;
    private Robot robot;

    public Mouse(int SLEEP_MS, int TOLERANCE){
        this.SLEEP_MS = SLEEP_MS;
        this.TOLERANCE = TOLERANCE;
        try{robot = new Robot();}catch (AWTException e){e.printStackTrace();}
    }

    public void changeSleep (int SLEEP_MS){
        this.SLEEP_MS = SLEEP_MS;
    }

    public void aim(int aimDist){
        int absAimDist = Math.abs(aimDist);
        if (SLEEP_MS != -1 && absAimDist >= TOLERANCE){
            Point mouse;
            int xMoveValue = aimDist < 0 ? -1: 1;

            for (int i = 1; i <= absAimDist; i++) {
                mouse = MouseInfo.getPointerInfo().getLocation();
                robot.mouseMove((int) mouse.getX() + xMoveValue, (int) mouse.getY());
                robot.delay(SLEEP_MS);
            }
        }
    }
}
