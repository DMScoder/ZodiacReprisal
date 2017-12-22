package com.zodiac.DATA;

import com.zodiac.World.Turret;

import java.util.ArrayList;

/**
 * Created by Immortan on 12/10/2017.
 */
public class Constants {

    public static final float DISPLAY_SHIP_TRHESHOLD = 10;
    public static final float DISPLAY_ICON_THRESHOLD = 5;

    //Factions
    public static final int FEDERATION = 0;
    public static final int PHOTONICS = 1;
    public static final int POWER_UPS = 2;

    //Ships
    public static final int PHOTONIC_SCOUT=0;
    public static final int FEDERATION_GUNBOAT=0;

    //Icon Classes
    public static final int ICON_CLASS=0;
    public static final int CORVETTE_CHASSIS=0;

    //Icon Types
    public static final int ICON_TYPE=1;
    public static final int SCOUT=0;

    //Powerups
    public static final int HEALTH=0; //Increases max health by 25% and restores full health
    public static final int DAMAGE=1; //Increases damage
    public static final int SPEED=2; //Increases max speed
    public static final int REGEN=3; //Increases health regeneration
    public static final int ACCURACY=4; //Decreases probability
    public static final int ROF=5; //Increases rate of fire
    public static final int NEW_SHIP=6; //Grants a free light ship

    public static final int MAX_HEALTH[][] = {
            {100},
            {100}};
    public static final float MAX_THRUST[][] = {
            {50},
            {50}};
    public static final float ACCELERATION[][] = {
            {5},
            {5}};
    public static final float TURN_SPEED[][] = {
            {15},
            {15}};
    public static final float SHIP_WIDTH[][] = {
            {50},
            {100}};
    public static final float SHIP_HEIGHT[][] = {
            {125},
            {125}};

    public static ArrayList<Turret> equipTurrets(int faction, int type) {
        ArrayList<Turret> turrets = new ArrayList<Turret>();
        if(faction==Constants.FEDERATION)
            switch (type){

            }
        else
            switch (type){

            }

        return turrets;
    }
}
