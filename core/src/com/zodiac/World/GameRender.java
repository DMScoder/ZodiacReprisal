package com.zodiac.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.zodiac.Support.AM;

import java.util.ArrayList;

/**
 * Created by Immortan on 12/11/2017.
 */
public class GameRender {

    ShapeRenderer shapeRenderer;
    GameState gameState;
    SpriteBatch gameBatch;

    public GameRender(GameState gameState){
            this.gameState = gameState;
            gameBatch = new SpriteBatch();
            shapeRenderer = new ShapeRenderer();
    }

    public void draw(OrthographicCamera camera){
        drawEntities(camera);
        drawSelected(camera);
        drawBounds(camera);
    }

    public void drawEntities(OrthographicCamera camera){
        gameBatch.setProjectionMatrix(camera.combined);
        gameBatch.begin();
        gameBatch.setColor(Color.WHITE);
        gameBatch.draw(AM.volcanic,-7500,-7500,15000,15000);

        ArrayList<Starship> starships = gameState.getShips();

        for(int i=0;i<starships.size();i++){
            starships.get(i).draw(gameBatch,camera.zoom);
        }

        ArrayList<PowerUp> powerUps = gameState.getPowerups();

        for(int i=0;i<powerUps.size();i++){
            powerUps.get(i).draw(gameBatch,camera.zoom,Color.WHITE);
        }

        gameBatch.end();
    }

    public void drawBounds(OrthographicCamera camera){
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.rect(-500000,-500000,1000000,1000000);
        shapeRenderer.end();
    }

    public void drawSelected(OrthographicCamera camera){
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 1, 0, 1);
        ArrayList<Starship> ships = gameState.getSelected();

        for(int i = 0;i<ships.size();i++) {
            ships.get(i).drawSelected(shapeRenderer,camera.zoom);
        }
        shapeRenderer.end();
    }
}
