import javafx.util.Pair;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by kevin on 1/17/17.
 */
public class GlareTrigger implements NativeMouseInputListener, NativeMouseWheelListener{
    private static Glare glare;
    private static ArrayList<Pair<String, Integer>> profiles;
    private static int profilePosition;
    private boolean disabled = true;

    public void nativeMouseClicked(NativeMouseEvent e) {}

    public void nativeMousePressed(NativeMouseEvent e) {
        if (!disabled && e.getButton() == 5)
            glare.unpause();
        else if (e.getButton() == 3) {
            glare.end();
            System.exit(1);
        }
    }

    public void nativeMouseReleased(NativeMouseEvent e) {
        if (e.getButton() == 5)
            glare.pause();
    }

    public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
        if (e.getWheelRotation() == 1){
            System.out.println(new String(new char[50]).replace("\0", "-"));

            int val = profiles.get(profilePosition).getValue();
            if (val == -1)
                disabled = true;
            else{
                disabled = false;
                glare.changeAimSpeed(val);
            }
            System.out.println("Switched to profile " + profiles.get(profilePosition).getKey());
            profilePosition = (profilePosition + 1) % profiles.size();
        }
    }

    public void nativeMouseMoved(NativeMouseEvent e) {}

    public void nativeMouseDragged(NativeMouseEvent e) {}

    public static void main(String[] args) {
        final int SCAN_HEIGHT = 250;
        final int SCAN_WIDTH = 250;
        final int TOLERANCE = 5; // aim tolerance in pixels (3 default)
        final boolean DEBUG = false;

        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook for keyboard/mouse event detection.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        profiles = new ArrayList<>();
        profiles.add(new Pair<>("Tracer", 2));
        profiles.add(new Pair<>("Zarya / Soldier", 3));
        profiles.add(new Pair<>("Disable", -1));
        profilePosition = 0;

        GlareTrigger program = new GlareTrigger();
        glare = new Glare(SCAN_HEIGHT, SCAN_WIDTH, DEBUG, 1, TOLERANCE);
        GlobalScreen.addNativeMouseListener(program);
        GlobalScreen.addNativeMouseWheelListener(program);
        glare.start();
    }
}