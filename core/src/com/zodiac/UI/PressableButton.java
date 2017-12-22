package com.zodiac.UI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Immortan on 12/10/2017.
 */
public class PressableButton {

    private Rectangle rec;
    private TextureRegion textureRegion;
    private TextureRegion textureRegion2;
    private boolean toggled = true;

    public PressableButton(TextureRegion textureRegion, float x, float y, float w, float h){
        this.textureRegion = textureRegion;
        rec = new Rectangle(x,y,w,h);
    }

    public PressableButton(TextureRegion on, TextureRegion off, float x, float y, float w, float h){
        this(on,x,y,w,h);
        textureRegion2 = off;
    }

    public boolean contains(float x, float y){
        return rec.contains(x,y);
    }

    public void setToggle(boolean b){
        toggled = b;
    }

    public boolean getToggle(){
        return toggled;
    }

    public void draw(SpriteBatch spriteBatch){
        if(textureRegion2==null) {
            if (!toggled)
                spriteBatch.setColor(1, 1, 1, 0.5f);
            spriteBatch.draw(textureRegion, rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
            spriteBatch.setColor(1, 1, 1, 1);
        }
        else{
            spriteBatch.draw(toggled?textureRegion:textureRegion2,rec.getX(),rec.getY(),rec.getWidth(),rec.getHeight());
        }
    }
}
