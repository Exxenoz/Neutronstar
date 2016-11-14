package at.autrage.projects.zeta.model;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

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

    public void updateGame(float deltaTime) {
        // Update game logic
    }

    @Override
    public void run() {
        m_Running = true;

        Canvas c = null;

        while (m_Running) {
            // TODO: Frame-Unabhängigkeit implementieren

            // Update:
            updateGame(0f);

            // Render:
            c = null;

            try {
                c = m_Holder.lockCanvas(null);
                synchronized (m_Holder) {
                    if (c != null) {
                        m_View.onDraw(c);
                    }
                }
            } finally {
                if (c != null) {
                    m_Holder.unlockCanvasAndPost(c);
                }
            }

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) { }
        }
    }
}
