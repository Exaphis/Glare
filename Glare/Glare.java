/**
 * Created by kevin on 1/21/2017.
 */
public class Glare extends Thread{
    private Screen s;
    private Mouse m;
    private final int AIM_ADJUST;
    private volatile boolean isPaused = true;
    private volatile boolean running = true;
    private volatile boolean disabled = false;

    public Glare(int scanHeight, int scanWidth, boolean debug, int MOUSE_SPEED, int AIM_TOLERANCE){
        s = new Screen(scanWidth, scanHeight, debug);
        m = new Mouse(MOUSE_SPEED, AIM_TOLERANCE);
        AIM_ADJUST = scanWidth/2;
    }

    public void run(){
        while (running){
            while (isPaused)
                try{
                    Thread.sleep(100);
                } catch (InterruptedException e){e.printStackTrace();}

            int aimDist = s.findPlayer();

            if (aimDist > 0) {
                aimDist -= AIM_ADJUST;
                m.aim(aimDist);
            }
        }
    }

    public void pause(){
        isPaused = true;
    }

    public void unpause(){
        isPaused = false;
    }

    public void end(){
        running = false;
    }

    public void changeAimSpeed (int speed){m.changeSleep(speed);}
}
