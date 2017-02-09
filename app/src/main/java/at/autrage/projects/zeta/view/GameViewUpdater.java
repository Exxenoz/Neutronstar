package at.autrage.projects.zeta.view;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.Time;

/**
 * This class contains the game loop.
 *
 * <p>If started, it repeats the update and render routine all 16 milli seconds.</p>
 */
public class GameViewUpdater implements Runnable {

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
    public GameViewUpdater(SurfaceHolder holder, GameView view) {
        this.m_Holder = holder;
        this.m_View = view;
    }

    /**
     * Set the value of {@link GameViewUpdater#m_Running}
     *
     * @param running True means that the thrad is currently running. Otherwise the variable is set to false.
     */
    public void setRunning(boolean running) {
        this.m_Running = running;
    }

    @Override
    public void run() {
        m_Running = true;

        Canvas c = null;
        long startTime = System.currentTimeMillis();
        long currTime = 0;
        long diffTime = 0;
        long sleepTime = 0;

        while (m_Running) {
            //************************************************
            //* Frame independent game logic updates section *
            //************************************************
            currTime = System.currentTimeMillis();

            diffTime = currTime - startTime;
            Time.setRealDeltaTime(diffTime);
            // The update delta may not be too high, otherwise physical calculations for movements or collisions
            // will fail due to float precision. In order to provide _always_ small update deltas, we simply
            // added this second loop, that sets delta time to a manageable maximum and calls the update() method.
            // This behaviour then is repeated until the remaining diff time is smaller than the specified max delta time.
            for (; diffTime >= Pustafin.MaxUpdateDelta; diffTime -= Pustafin.MaxUpdateDelta) {
                Time.setDeltaTimeInMs(Pustafin.MaxUpdateDelta);
                Time.setDeltaTime(Pustafin.MaxUpdateDeltaInSeconds);
                if (Time.getTimeScale() != 0f) {
                    m_View.update();
                }
            }

            if (diffTime > 0) {
                Time.setDeltaTimeInMs(diffTime);
                Time.setDeltaTime(diffTime / 1000f);
                if (Time.getTimeScale() != 0f) {
                    m_View.update();
                }
            }

            startTime = currTime;

            //**********************************
            //* Synchronized rendering section *
            //**********************************
            c = null;

            try {
                c = m_Holder.lockCanvas(null);
                synchronized (m_Holder) {
                    if (c != null) {
                        m_View.render(c);
                    }
                }
            } finally {
                if (c != null) {
                    m_Holder.unlockCanvasAndPost(c);
                }
            }

            sleepTime = Pustafin.MinRenderDelta - (System.currentTimeMillis() - startTime);
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
