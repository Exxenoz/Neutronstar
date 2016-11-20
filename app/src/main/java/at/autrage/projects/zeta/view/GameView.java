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

/**
 * It is responsible for {@link GameLoop} management and drawing of the whole game view.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    /** Reference to our {@link GameLoop} object. */
    GameLoop m_Loop;
    /** Reference to our {@link GameLoop} thread. */
    Thread m_LoopThread;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Add callback for events
        getHolder().addCallback(this);
        // Ensure that events are generated
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("PNE::Debug", "GameView::surfaceCreated()");
        // Initialize game loop and start thread
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
        // ToDo: Implement input processing
        return true;
    }

    /**
     * This method draws the game view to the given {@link Canvas} object.
     *
     * @param canvas The canvas where the game view should be drawn.
     */
    public void drawGameView(Canvas canvas) {
        // Create paint object
        Paint p = new Paint();
        p.setColor(Color.RED);
        // Draw rectangle for testing purpose
        canvas.drawRect(20, 20, 100, 100, p);
    }

    /**
     * This method stops the game loop thread.
     */
    private void endGame() {
        m_Loop.setRunning(false);

        try {
            m_LoopThread.join();
        } catch (InterruptedException e) {
            Log.e("PNE::Error", e.getMessage());
        }
    }
}
