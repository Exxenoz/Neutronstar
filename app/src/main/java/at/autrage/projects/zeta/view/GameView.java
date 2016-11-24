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
import android.widget.TextView;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.activity.GameActivity;
import at.autrage.projects.zeta.model.GameLoop;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.module.Time;

/**
 * It is responsible for {@link GameLoop} management and drawing of the whole game view.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    /** Reference to our {@link GameActivity} object. */
    GameActivity m_GameActivity;
    /** Reference to our {@link GameLoop} object. */
    GameLoop m_Loop;
    /** Reference to our {@link GameLoop} thread. */
    Thread m_LoopThread;
    /** Reference to the fps text view element. */
    TextView m_TxtViewFPS;
    /** Reference to our {@link GameViewAssets} object. */
    GameViewAssets m_Assets;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Initialize game activity variable
        m_GameActivity = (GameActivity)context;
        // Add callback for events
        getHolder().addCallback(this);
        // Ensure that events are generated
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("PNE::Debug", "GameView::surfaceCreated()");

        m_Assets = new GameViewAssets();
        m_Assets.load(getResources());

        SoundManager.getInstance().StartBGM(R.raw.cantina_band, true);

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

        m_Loop.setRunning(false);

        try {
            m_LoopThread.join();
        } catch (InterruptedException e) {
            Log.e("PNE::Error", e.getMessage());
        }

        SoundManager.getInstance().StopBGM();

        m_Assets.unLoad();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // ToDo: Implement input processing
        return true;
    }

    /**
     * Function which updates the game models
     */
    public void update() {
        // Update user interface states
        m_GameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (m_TxtViewFPS != null) {
                    m_TxtViewFPS.setText(Time.getFPS() + " FPS");
                }
            }
        });
    }

    /**
     * This method draws the game view to the given {@link Canvas} object.
     *
     * @param canvas The canvas where the game view should be drawn.
     */
    public void render(Canvas canvas) {
        canvas.drawBitmap(m_Assets.BackgroundGame, m_Assets.BackgroundGameRectSrc, m_Assets.BackgroundGameRectDst, new Paint());
    }

    /**
     * Sets the value of {@link GameView#m_TxtViewFPS}
     *
     * @param txtViewFPS The reference to the fps text view.
     */
    public void setTxtViewFPS(TextView txtViewFPS) {
        m_TxtViewFPS = txtViewFPS;
    }
}
