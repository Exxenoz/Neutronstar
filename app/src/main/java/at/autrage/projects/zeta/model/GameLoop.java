package at.autrage.projects.zeta.model;

import android.graphics.Canvas;
import android.provider.Settings;
import android.view.SurfaceHolder;

import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.view.GameView;

public class GameLoop implements Runnable {

    private SurfaceHolder m_Holder;
    private GameView m_View;
    private boolean m_Running;

    public GameLoop(SurfaceHolder holder, GameView view) {
        this.m_Holder = holder;
        this.m_View = view;
    }

    public void setRunning(boolean running) {
        this.m_Running = running;
    }

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

            sleepTime = 16 - System.currentTimeMillis() - startTime;
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
