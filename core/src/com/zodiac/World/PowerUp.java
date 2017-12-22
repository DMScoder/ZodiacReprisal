package com.zodiac.World;

import com.zodiac.DATA.Constants;
import com.zodiac.Support.AM;

/**
 * Created by Immortan on 12/17/2017.
 */
public class PowerUp extends Entity {
    public PowerUp(float x, float y, int type, int angle, int thrust) {
        super(x, y);

        this.getPolygon().setRotation(angle);
        this.setThrust(thrust);

        setGraphics(128,128, AM.allEntities[Constants.POWER_UPS][type],AM.allEntityIcons[Constants.POWER_UPS][type],null);
    }
}
