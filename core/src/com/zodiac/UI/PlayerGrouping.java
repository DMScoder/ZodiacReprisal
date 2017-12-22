package com.zodiac.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zodiac.DATA.Constants;
import com.zodiac.Support.AM;

/**
 * Created by Immortan on 12/10/2017.
 */
public class PlayerGrouping {

    private String name;
    private int faction;
    private boolean ready;
    private int order;

    public PlayerGrouping(String name, int faction, boolean ready, int order){
        this.name = name;
        this.faction = faction;
        this.ready = ready;
        this.order = order;
    }

    public void draw(SpriteBatch batch, BitmapFont bitmapFont){
        bitmapFont.setColor(Color.WHITE);
        if(faction==Constants.FEDERATION)
            bitmapFont.setColor(Color.RED);
        if(faction==Constants.PHOTONICS)
            bitmapFont.setColor(Color.BLUE);

        bitmapFont.draw(batch,name, Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight() - order*32);

        if(faction!=-1)batch.draw(AM.fIcons[faction],Gdx.graphics.getWidth()*5/8,Gdx.graphics.getHeight() - order*32-16,32,32);

        bitmapFont.setColor(ready?Color.GREEN:Color.RED);
        bitmapFont.draw(batch,ready?"Ready":"Not ready",Gdx.graphics.getWidth()*5/8+36,Gdx.graphics.getHeight() - order*32);
        bitmapFont.setColor(Color.WHITE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFaction() {
        return faction;
    }

    public void setFaction(int faction) {
        this.faction = faction;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
