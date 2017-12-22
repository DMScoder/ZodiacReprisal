package com.zodiac.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.zodiac.DATA.Constants;
import com.zodiac.Support.AM;

/**
 * Created by Immortan on 12/10/2017.
 */
public class ShipGroup {

    private float x,y,width,height;
    private String description = "";
    private String title = "";
    private int cost = 0;
    private PressableButton increment;
    private PressableButton decrement;
    private PressableButton shipIcon;
    private int ID;
    private int amount;
    private int faction;

    public ShipGroup(float x, float y, float width, float height, int faction, int ID){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.ID = ID;
        this.faction = faction;

        setText();

        shipIcon = new PressableButton(AM.allEntities[faction][0],x,y+height/2,height/2,height/2);
        increment = new PressableButton(AM.utilIcons[AM.PLUS],x+height/20,y+height/20,height/10,height/10);
        decrement = new PressableButton(AM.utilIcons[AM.MINUS],x+width/2-height/20-height/10,y+height/20,height/10,height/10);
    }

    public void drawShapeRender(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(faction==Constants.FEDERATION? Color.RED:Color.BLUE);
        shapeRenderer.rect(x,y,width,height);
        shapeRenderer.rect(x,y+height/2,width/2,height/2);
    }

    public void drawBatch(SpriteBatch batch, BitmapFont bitmapFont){
        shipIcon.draw(batch);
        batch.setColor(Color.GREEN);
        increment.draw(batch);
        batch.setColor(Color.RED);
        decrement.draw(batch);
        batch.setColor(Color.WHITE);
        bitmapFont.draw(batch,title,x+width/2,height+y-15,width/2-15, Align.center,false);
        bitmapFont.draw(batch,description,x+width/2+7,height+y-45,width/2-15, Align.left,true);
        bitmapFont.draw(batch,"Cost: "+cost,x,y+height/2-20,width/2-15,Align.center,false);
        bitmapFont.draw(batch,"Amount: "+amount,x,y+height/4,width/2-15,Align.center,false);
    }

    //Returns amount spent or gained
    public int checkClick(float x, float y, int creditsLeft){
        if(increment.contains(x,y)){
            if(creditsLeft>=cost) {
                amount++;
                return -cost;
            }
        }
        else if(decrement.contains(x,y)){
            if(amount>0){
                amount--;
                return cost;
            }
        }

        return 0;
    }

    public int getFaction(){
        return faction;
    }

    public int getAmount(){
        return amount;
    }

    private void setText(){
        if(faction==Constants.FEDERATION)
            switch (ID){
                case(0):
                    title="Gunboat";
                    description="Fast skirmish vessel great at finishing off weaker ships and scouting. Good range, firepower, and armor for a corvette class ship.";
                    cost=50;
                    break;
                case(1):
                    title="Missile Hunter";
                    description="Very long range, slow corvette. Does a lot of damage but has very low survivability and visibility.";
                    cost=75;
                    break;
                case(2):
                    title="Federation Destroyer";
                    description="Sleek and agile hunter killer ship. Poor armor but excellent damage and range.";
                    cost=200;
                    break;
                case(3):
                    title="Line Frigate";
                    description="Mid range, versatile weapons platform capable of taking a beating.";
                    cost=225;
                    break;
                case(4):
                    title="Baelon Battlecruiser";
                    description="Most damage dealing ship in the Federation fleet. Smaller range then the destroyer, and less survivability then the cruiser.";
                    cost=400;
                    break;
                case(5):
                    title="Ironside Cruiser";
                    description="Slow, heavily armored weapons platform. Does not take damage from ramming other ships.";
                    cost=500;
                    break;
                case(6):
                    title="AWAC Frigate";
                    description="Lightly armed radar ship. Can reveal any area on the map briefly.";
                    cost=250;
                    break;

            }
        else
            switch (ID){
                case(0):
                    title="Photonic scout";
                    description="Good range, fast, support platform. Can warp short distances.";
                    cost=75;
                    break;
                case(1):
                    title="Light Craft";
                    description="Fast, cheap, close range vessel with considerable damage.";
                    cost=25;
                    break;
                case(2):
                    title="Laser Destroyer";
                    description="Extremely accurate, damage dealing, mid range ship.";
                    cost=200;
                    break;
                case(3):
                    title="Optical Frigate";
                    description="Frigate that shares health with up to five nearby optical frigates. Dependable.";
                    cost=175;
                    break;
                case(4):
                    title="Lightbringer";
                    description="Very poorly armed utility vessel. Can repair nearby ships.";
                    cost=250;
                    break;
                case(5):
                    title="Helios Cruiser";
                    description="Large photonic vessel capable of going toe to toe against other, larger vessels at mid range.";
                    cost=375;
                    break;
                case(6):
                    title="Light Carrier";
                    description="Vulnerable, large vessel that spawns a light craft every minute.";
                    cost=600;
                    break;
            }
    }
}
