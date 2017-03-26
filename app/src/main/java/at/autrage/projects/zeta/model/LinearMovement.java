package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Time;

public class LinearMovement extends Component {
    private float directionX;
    private float directionY;
    private float speedX;
    private float speedY;
    private float speed;

    public LinearMovement(GameObject gameObject) {
        super(gameObject);

        this.directionX = 0f;
        this.directionY = 0f;
        setSpeed(0f);
    }

    public void initialize(float directionX, float directionY, float speed) {
        this.directionX = directionX;
        this.directionY = directionY;
        setSpeed(speed);
    }

    public void setDirection(float directionX, float directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
        setSpeed(speed);
    }

    public float getDirectionX() {
        return directionX;
    }

    public void setDirectionX(float directionX) {
        this.directionX = directionX;
        setSpeed(speed);
    }

    public float getDirectionY() {
        return directionY;
    }

    public void setDirectionY(float directionY) {
        this.directionY = directionY;
        setSpeed(speed);
    }

    public float getSpeedX() {
        return speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        this.speedX = directionX * speed;
        this.speedY = directionY * speed;

    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        if (speed != 0f) {
            gameObject.setPosition(
                    gameObject.getPositionX() + speedX * Time.getScaledDeltaTime(),
                    gameObject.getPositionY() + speedY * Time.getScaledDeltaTime()
            );
        }
    }
}
