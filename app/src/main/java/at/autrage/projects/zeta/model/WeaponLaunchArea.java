package at.autrage.projects.zeta.model;

import java.util.HashMap;
import java.util.Map;

import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.ui.TouchDown;
import at.autrage.projects.zeta.ui.TouchEvent;
import at.autrage.projects.zeta.ui.TouchUpGlobal;

public class WeaponLaunchArea extends Component implements TouchDown, TouchUpGlobal {
    private Map<Integer, Vector2D> m_TouchEventStartPositions;

    public WeaponLaunchArea(GameObject gameObject) {
        super(gameObject);

        m_TouchEventStartPositions = new HashMap<>();
    }

    @Override
    public void touchDown(Collider collider, TouchEvent e) {
        if (gameObject.getGameView().isLevelFinished()) {
            return;
        }

        if (Time.getTimeScale() == 0f) {
            m_TouchEventStartPositions.clear();
            return;
        }

        Vector2D pos = new Vector2D();
        pos.X = e.WorldCoordX;
        pos.Y = e.WorldCoordY;
        m_TouchEventStartPositions.put(e.ActionIndex, pos);
    }

    @Override
    public void touchUpGlobal(Collider collider, TouchEvent e) {
        if (gameObject.getGameView().isLevelFinished()) {
            return;
        }

        if (Time.getTimeScale() == 0f) {
            m_TouchEventStartPositions.clear();
            return;
        }

        Vector2D startTouchPosition = m_TouchEventStartPositions.get(e.ActionIndex);
        if (startTouchPosition == null) {
            return;
        }

        m_TouchEventStartPositions.remove(e.ActionIndex);

        Weapons selectedWeapon = gameObject.getGameView().getPlayer().getSelectedWeapon();
        WeaponStockpile weaponStockpile = GameManager.getInstance().getWeaponStockpile(selectedWeapon);
        if (weaponStockpile == null) {
            return;
        }

        int weaponCount = weaponStockpile.getCount();
        if (weaponCount == 0) {
            return;
        }

        float deltaPositionX = e.WorldCoordX - startTouchPosition.X;
        float deltaPositionY = e.WorldCoordY - startTouchPosition.Y;

        double distance = Math.sqrt(deltaPositionX * deltaPositionX + deltaPositionY * deltaPositionY);
        if (distance < Pustafin.MinSwipeDistance) {
            return;
        }

        float directionX = (float) (deltaPositionX / distance);
        float directionY = (float) (deltaPositionY / distance) * (-1); // Flip y direction

        float radius = Pustafin.PlanetScale / 2f;

        float spawnPositionX = directionX * radius;
        float spawnPositionY = directionY * radius;

        Weapon weapon = null;
        switch (selectedWeapon) {
            case SmallRocket:
                weapon = Rocket.createSmallRocket(gameObject.getGameView(), spawnPositionX, spawnPositionY, directionX, directionY);
                break;
            case BigRocket:
                weapon = Rocket.createBigRocket(gameObject.getGameView(), spawnPositionX, spawnPositionY, directionX, directionY);
                break;
            case SmallNuke:
                weapon = Nuke.createSmallNuke(gameObject.getGameView(), spawnPositionX, spawnPositionY, directionX, directionY);
                break;
            case BigNuke:
                weapon = Nuke.createBigNuke(gameObject.getGameView(), spawnPositionX, spawnPositionY, directionX, directionY);
                break;
            case SmallContactBomb:
                break;
            case BigContactBomb:
                break;
        }

        Logger.D("Spawned selected weapon %s at (%f, %f) with direction (%f, %f), hit damage %f.",
                selectedWeapon.toString(), spawnPositionX, spawnPositionY, directionX, directionY, weapon.getHitDamage());

        if (weaponCount > 0) {
            weaponStockpile.setCount(--weaponCount);
            if (weaponCount == 0) {
                gameObject.getGameView().getGameActivity().setHighlightedHotbarBoxToSmallRocketArea();
            }
        }
    }
}
