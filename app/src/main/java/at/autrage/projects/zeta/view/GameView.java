package at.autrage.projects.zeta.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import at.autrage.projects.zeta.model.GameLoop;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    GameLoop m_Loop;
    Thread m_LoopThread;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Callback für Events hinzufügen:
        getHolder().addCallback(this);
        // Damit Events behandelt werden:
        setFocusable(true);
        // z.B. Ressourcen initialisieren
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("PNE::Debug", "GameView::surfaceCreated()");
        m_Loop = new GameLoop(holder, this);
        m_LoopThread = new Thread(m_Loop);
        m_LoopThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("PNE::Debug", "GameView::surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("PNE::Debug", "GameView::surfaceDestroyed()");
        endGame();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // Ein Touch-Event wurde ausgelöst
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            // Was soll bei einer Berührung passieren?
        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Paint erstellen:
        Paint p = new Paint();
        p.setColor(Color.RED);
        // Rechteck zeichnen:
        canvas.drawRect(20, 20, 100, 100, p);
    }

    // Stop the game loop and the thread:
    private void endGame() {
        m_Loop.setRunning(false);

        try {
            m_LoopThread.join();
        } catch (InterruptedException e) {
            Log.e("PNE::Error", e.getMessage());
        }
    }
}
