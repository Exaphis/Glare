import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kevin on 1/17/17.
 */
public class Screen {
    private final int[] HEALTH = {255, 0, 19};
    private Rectangle screenRect;
    private Robot robot;
    private boolean hpInLastScan;
    private int lastMidDist;
    private boolean debug;

    public Screen(int scanWidth, int scanHeight, boolean debug) {
        try {robot = new Robot();} catch (AWTException e){e.printStackTrace();}
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.debug = debug;
        int startX = (screenDimension.width - scanWidth)/2;
        int startY = (screenDimension.height - scanHeight)/2;
        screenRect = new Rectangle(startX, startY, scanWidth, scanHeight);

        hpInLastScan = false;
        lastMidDist = 0;
    }

    public int findPlayer(){
        BufferedImage scr = robot.createScreenCapture(screenRect);
        RGB rgbValues = new RGB(scr);
        for (int x = 0; x < screenRect.width; x++) {
            for (int y = 0; y < screenRect.height; y++) {

                if (isHealth(rgbValues.getRGB(x, y))) {
                    int barSize = 0;

                    if (!hpInLastScan) {
                        int barX = x + 1;
                        while (barX < screenRect.width && isHealth(rgbValues.getRGB(barX, y))) {
                            barSize++;
                            barX++;
                        }
                        lastMidDist = findHPMiddle(barSize);
                    }

                    if (debug) {
                        debugAim(scr, x, "aimStartDebug");
                        debugAim(scr, x + lastMidDist, "aimMiddleDebug");
                        if (barSize != 0)
                            System.out.println("HP bar size: " + barSize + " px");
                        else
                            System.out.println("Past HP bar detected, using last middle distance of " + lastMidDist);
                    }

                    hpInLastScan = true;

                    return x + lastMidDist;
                }
            }
        }
        hpInLastScan = false;
        return -1;
    }

    private int findHPMiddle(int barSize){
        if (barSize < 8)
            return 45;
        return (5*barSize) + 2;
    }

    private boolean isHealth(int[] rgb){
        return Arrays.equals(rgb, HEALTH);
    }

    private void debugAim(BufferedImage scr, int x, String fileName){
        for(int y = 0; y < scr.getHeight(); y++)
            scr.setRGB(x, y, Color.BLUE.getRGB());
        try{ImageIO.write(scr, "png", new File(fileName + ".png"));}catch (IOException e){e.printStackTrace();}
    }

    class RGB {
        BufferedImage image;

        RGB(BufferedImage image) {
            this.image = image;
        }

        int[] getRGB(int x, int y) {
            int[] rgb = new int[3];
            int clr = image.getRGB(x, y);
            rgb[0] = (clr & 0x00ff0000) >> 16;
            rgb[1] = (clr & 0x0000ff00) >> 8;
            rgb[2] = clr & 0x000000ff;
            return rgb;
        }
    }
}
