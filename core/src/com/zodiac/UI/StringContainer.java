package com.zodiac.UI;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Immortan on 12/10/2017.
 */
public class StringContainer {

    private String string;
    private float x,y,width;
    private int align;

    public StringContainer(String string, float x, float y, int align, float width){
        this.string = string;
        this.x=x;
        this.y=y;
        this.align = align;
        this.width=width;
    }

    public void draw(SpriteBatch batch, BitmapFont bitmapFont){
        bitmapFont.draw(batch,string,x,y,width, align,true);
    }
}
