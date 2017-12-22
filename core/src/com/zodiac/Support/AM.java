package com.zodiac.Support;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.zodiac.DATA.Constants;

/**
 * Created by Immortan on 12/9/2017.
 */
public class AM {

    public static final int PLUS = 1;
    public static final int MINUS = 2;
    public static final int CHECK = 3;
    public static final int X = 0;


    public static TextureRegion fIcons[] = new TextureRegion[2];
    public static TextureRegion utilIcons[] = new TextureRegion[4];
    public static TextureRegion allEntities[][] = new TextureRegion[3][7];
    public static TextureRegion allEntityIcons[][] = new TextureRegion[3][7];
    public static TextureRegion volcanic;


    private static TextureRegion loadTexture (String file) {
        return new TextureRegion(new Texture(Gdx.files.internal(file)));
    }

    public static void loadAll(){
        volcanic = loadTexture("Volcanic.png");

        fIcons[Constants.FEDERATION] = loadTexture("eagle2.png");
        fIcons[Constants.PHOTONICS] = loadTexture("PhotonicReapers2.png");

        utilIcons[PLUS] = loadTexture("plus.png");
        utilIcons[MINUS] = loadTexture("minus.png");
        utilIcons[X] = loadTexture("notready.png");
        utilIcons[CHECK] = loadTexture("ready.png");

        allEntities[Constants.FEDERATION][Constants.FEDERATION_GUNBOAT] = loadTexture("F_Gunboat.png");
        allEntities[Constants.PHOTONICS][Constants.PHOTONIC_SCOUT] = loadTexture("P_Scout.png");

        allEntityIcons[Constants.ICON_CLASS][Constants.CORVETTE_CHASSIS]=loadTexture("Icon_Class_Corvette.png");
        allEntityIcons[Constants.ICON_TYPE][Constants.SCOUT]=loadTexture("Icon_Type_Scout.png");

        allEntities[Constants.POWER_UPS][Constants.HEALTH] = loadTexture("PowerUpHealth.png");
        allEntities[Constants.POWER_UPS][Constants.DAMAGE] = loadTexture("PowerUpDamage.png");
        allEntities[Constants.POWER_UPS][Constants.SPEED] = loadTexture("PowerUpSpeed.png");
        allEntities[Constants.POWER_UPS][Constants.ROF] = loadTexture("PowerUpROF.png");
        allEntities[Constants.POWER_UPS][Constants.NEW_SHIP] = loadTexture("PowerUpShip.png");
        allEntities[Constants.POWER_UPS][Constants.REGEN] = loadTexture("PowerUpRegen.png");
        allEntities[Constants.POWER_UPS][Constants.ACCURACY] = loadTexture("PowerUpAccuracy.png");

        allEntityIcons[Constants.POWER_UPS][Constants.NEW_SHIP] = loadTexture("IconShip.png");
    }
}
