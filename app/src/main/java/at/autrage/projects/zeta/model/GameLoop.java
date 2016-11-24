package at.autrage.projects.zeta.model;

import android.graphics.Canvas;
import android.provider.Settings;
import android.view.SurfaceHolder;

import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.view.GameView;

/**
 * This class contains the game loop.
 *
 * <p>If started, it repeats the update and render routine all 16 milli seconds.</p>
 */
public class GameLoop implements Runnable {

    /** Variable for locking the canvas while rendering */
    private SurfaceHolder m_Holder;
    /** Variable which stores reference to {@link GameView} */
    private GameView m_View;
    /**
     * Variable which contains the current state of the thread.
     *
     * <p>True means that the thrad is currently running. Otherwise the variable is set to false.</p>
     */
    private boolean m_Running;

    /**
     * Constructor
     *
     * @param holder Reference to the canvas which has to be locked
     * @param view Reference to the {@link GameView}
     */
    public GameLoop(SurfaceHolder holder, GameView view) {
        this.m_Holder = holder;
        this.m_View = view;
    }

    /**
     * Set the value of {@link GameLoop#m_Running}
     *
     * @param running True means that the thrad is currently running. Otherwise the variable is set to false.
     */
    public void setRunning(boolean running) {
        this.m_Running = running;
    }

    /**
     * Function which updates the game models
     */
    public void updateGame() {
        // Update game logic
    }

    @Override
    public void run() {
        m_Running = true;

        Canvas c = null;
        long startTime = System.currentTimeMillis();
        long currTime = 0;
        long sleepTime = 0;

        while (m_Running) {
            // Time:
            currTime = System.currentTimeMillis();

            Time.setDeltaTimeInMs(currTime - startTime);
            Time.setDeltaTime(Time.getDeltaTimeInMs() / 1000f);

            startTime = currTime;

            // Update:
            updateGame();

            // Render:
            c = null;

            try {
                c = m_Holder.lockCanvas(null);
                synchronized (m_Holder) {
                    if (c != null) {
                        m_View.drawGameView(c);
                    }
                }
            } finally {
                if (c != null) {
                    m_Holder.unlockCanvasAndPost(c);
                }
            }

            sleepTime = 16 - (System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                // Do nothing, in Andi we trust!
            }
        }
    }
}
