package com.zodiac.World;

/**
 * Created by Immortan on 12/11/2017.
 */
public class Turret extends Entity {

    float MAX_MAGAZINE = 0;
    float CURRENT_MAGAZINE = 0;
    float RELOAD_PERIOD = 0;
    float MAGAZINE_TIME = 0;
    float RANGE = 0;
    float DAMAGE = 0;
    float SPEED = 1000;
    float ROTATE_SPEED = 90;

    public Turret(float hostCenterX, float hostCenterY, Starship host, int type) {
        super(hostCenterX,hostCenterY);

    }

    public void update(){

    }
}
