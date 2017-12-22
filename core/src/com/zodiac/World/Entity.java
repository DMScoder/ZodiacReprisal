package com.zodiac.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.zodiac.DATA.Constants;
import com.zodiac.Support.AM;

/**
 * Created by Immortan on 12/11/2017.
 */
public abstract class Entity {


    private float x,y,w,h,targetX,targetY,currentThrust,currentVelocityAngle;
    private TextureRegion mainTexture, iconBaseTexture, iconSecondaryTexture;
    private Polygon polygon;

    public Entity(float x, float y){
        this(x,y,0,0,0,null,null,null);
        this.x=x;
        this.y=y;
        this.targetX = x;
        this.targetY = y;
    }

    public Entity(float x, float y, float w, float h, float angle,
                  TextureRegion mainTexture, TextureRegion iconBaseTexture,TextureRegion iconSecondaryTexture){
        this.mainTexture = mainTexture;
        this.iconBaseTexture = iconBaseTexture;
        this.iconSecondaryTexture = iconSecondaryTexture;
        this.x=x;
        this.y=y;

        polygon = new Polygon(new float[]{0,0,w,0,w,h,0,h});
        polygon.setOrigin(w/2,h/2);
        polygon.setPosition(x,y);
        polygon.setRotation(angle);
    }

    public void setGraphics(float w, float h,
                            TextureRegion mainTexture, TextureRegion iconBaseTexture, TextureRegion iconSecondaryTexture){
        this.w=w;
        this.h=h;
        polygon = new Polygon(new float[]{0,0,w,0,w,h,0,h});
        polygon.setOrigin(w/2,h/2);
        polygon.setPosition(x,y);
        this.mainTexture = mainTexture;
        this.iconBaseTexture = iconBaseTexture;
        this.iconSecondaryTexture = iconSecondaryTexture;
    }

    //Return if collision
    public Entity updatePosition(float delta){

        /*
        if(getY()-targetY > 10)
            getPolygon().translate(0, -currentThrust*delta);
        else if(getY()-targetY < -10)
            getPolygon().translate(0,currentThrust*delta);
        if(getX()-targetX > 10)
            getPolygon().translate(-currentThrust*delta,0);
        else if(getX()-targetX < -10)
            getPolygon().translate(currentThrust*delta,0);
            */

        getPolygon().translate(MathUtils.cosDeg(getPolygon().getRotation())* currentThrust*delta,
                MathUtils.sinDeg(getPolygon().getRotation())* currentThrust*delta);

        //getPolygon().setRotation(-MathUtils.radiansToDegrees * MathUtils.atan2(targetX-getX(),targetY-getY()));

        return null;
    }

    public void draw(SpriteBatch batch, float zoom, Color playerColor){
        if(zoom > Constants.DISPLAY_SHIP_TRHESHOLD && iconBaseTexture ==null)
            return;

        if(zoom <= Constants.DISPLAY_SHIP_TRHESHOLD) {
            batch.setColor(Color.WHITE);
            batch.draw(mainTexture.getTexture(), polygon.getX(), polygon.getY(), polygon.getOriginX(), polygon.getOriginY(), w,
                    h, polygon.getScaleX(), polygon.getScaleY(), polygon.getRotation(), 0, 0,
                    mainTexture.getRegionWidth(), mainTexture.getRegionHeight(), false, false);
        }

        if(zoom > Constants.DISPLAY_ICON_THRESHOLD) {
            batch.setColor(playerColor);
            batch.draw(iconBaseTexture,polygon.getX()+w/2-10*zoom,polygon.getY()+h/2-10*zoom,20*zoom,20*zoom);
            batch.setColor(Color.BLACK);

            if(iconSecondaryTexture!=null)
                batch.draw(iconSecondaryTexture,polygon.getX()+w/2-10*zoom,polygon.getY()+h/2-10*zoom,20*zoom,20*zoom);
        }
    }

    public void drawSelected(ShapeRenderer shapeRenderer, float zoom){
        if(zoom < Constants.DISPLAY_ICON_THRESHOLD) {
            shapeRenderer.polygon(getPolygon().getTransformedVertices());
        }

        else{
            shapeRenderer.rect(polygon.getX()-10*zoom+w/2,polygon.getY()-10*zoom+h/2,20*zoom,20*zoom);
        }
    }

    public void setThrust(float f){
        currentThrust = f;
    }

    public void setTargetCoord(float targetX, float targetY){
        this.targetY = targetY;
        this.targetX = targetX;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public float getX(){return polygon.getX();}

    public float getY(){return polygon.getY();}
}
