package com.zodiac.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zodiac.DATA.Constants;
import com.zodiac.Support.AM;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Immortan on 12/11/2017.
 */
public class Starship extends Entity implements Attackable{

    private Player player;
    private int faction;
    private int type, ID;

    private float moveTargetX, moveTargetY;
    private Entity attackTarget;

    private int health, maxHealth;
    private ArrayList<Turret> turrets;

    public Starship(Player player, int faction, int x, int y, int type, int ID){
        super(x,y);
        this.player=player;
        this.faction=faction;
        this.type=type;
        this.ID = ID;

        fillInStats();
    }

    //Return if shots fired
    public ArrayList<Projectile> updateTurrets(){

        return null;
    }

    public void draw(SpriteBatch batch, float zoom){
        super.draw(batch,zoom,player.getColor());
        /*
        batch.draw(AM.allEntities[faction][type],x,y,
                Constants.SHIP_WIDTH[faction][type]/2,Constants.SHIP_HEIGHT[faction][type]/2,
                Constants.SHIP_WIDTH[faction][type],Constants.SHIP_HEIGHT[faction][type],1,1,currentShipAngle);*/
    }

    private void fillInStats(){
        health = maxHealth();
        maxHealth = health;
        turrets = Constants.equipTurrets(faction,type);

        switch (faction){
            case Constants.FEDERATION:
                switch (type){

                }
                break;
            case Constants.PHOTONICS:
                switch (type){

                }
                break;
        }
        this.setGraphics(Constants.SHIP_WIDTH[faction][type],
                Constants.SHIP_HEIGHT[faction][type],
                AM.allEntities[faction][type],
                AM.allEntityIcons[Constants.ICON_CLASS][Constants.CORVETTE_CHASSIS],
                AM.allEntityIcons[Constants.ICON_TYPE][Constants.SCOUT]);
    }

    public void setMoveOrder(float targetX, float targetY){
        this.setTargetCoord(targetX,targetY);
    }

    public Player getPlayer(){
        return player;
    }

    public int getID(){
        return ID;
    }

    public float maxThrust(){
        return Constants.MAX_THRUST[faction][type];
    }

    public int maxHealth(){
        return Constants.MAX_HEALTH[faction][type];
    }

    public float maxTurnSpeed(){
        return Constants.MAX_HEALTH[faction][type];
    }

}
