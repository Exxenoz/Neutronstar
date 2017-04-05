package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.module.Time;

/**
 * This class represents the alarm area around the planet.
 */
public class AlarmArea extends Component {
    /**
     * Indicates whether the alarm is enabled or not.
     */
    private boolean alarmEnabled;

    /**
     * True if the alarm should be stopped automatically, otherwise false.
     */
    private boolean alarmAutoStop;

    /**
     * Timer for smooth alarm foreground blinking.
     */
    private float alarmTimer;

    /**
     * Reference to the alarm foreground sprite.
     */
    private Sprite alarmForegroundSprite;

    public AlarmArea(GameObject gameObject) {
        super(gameObject);

        alarmEnabled = false;
        alarmAutoStop = true;
        alarmTimer = 0f;
        alarmForegroundSprite = null;
    }

    @Override
    protected void onStart() {
        alarmForegroundSprite = gameObject.getComponent(Sprite.class);
    }

    @Override
    public void onCollide(Collider other) {
        if (other.getGameObject().getComponent(Enemy.class) != null) {
            if (!isAlarmEnabled()) {
                setAlarmEnabled(true);
            }
        }
    }

    @Override
    protected void onUpdate() {
        if ((alarmEnabled || alarmTimer > 0f) && alarmForegroundSprite != null) {
            if (alarmTimer == 0f && !gameObject.getGameView().isLevelFinished()) {
                SoundManager.getInstance().PlaySFX(R.raw.sfx_siren_noise);
            }

            if (alarmAutoStop) {
                alarmEnabled = false;
            }

            alarmTimer += Time.getScaledDeltaTime();
            if (alarmTimer >= Pustafin.AlarmForegroundBlinkDuration) {
                alarmTimer = 0f;
            }

            alarmForegroundSprite.setAlpha((float) Math.sin(Math.PI * alarmTimer / Pustafin.AlarmForegroundBlinkDuration));
        }
    }

    public boolean isAlarmEnabled() {
        return alarmEnabled;
    }

    public void setAlarmEnabled(boolean alarmEnabled) {
        this.alarmEnabled = alarmEnabled;
    }

    public boolean isAlarmAutoStopped() {
        return alarmAutoStop;
    }

    public void setAlarmAutoStop(boolean alarmAutoStop) {
        this.alarmAutoStop = alarmAutoStop;
    }
}
