package com.zodiac.World;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Immortan on 12/11/2017.
 */
public class Player {

    private int ID;
    private String name;
    private Color color;
    private static int playersCreated=0;
    public int ships_owned=0;

    public Player(int playerID, String playerName){
        this.ID = playerID;
        this.name = playerName;
        setColor();
        playersCreated++;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private void setColor(){
        switch (playersCreated){
            case 0:
                color = Color.RED;
                break;
            case 1:
                color = Color.YELLOW;
                break;
            case 2:
                color = Color.GREEN;
                break;
            case 3:
                color = Color.BLUE;
                break;
            case 4:
                color = Color.CYAN;
                break;
            case 5:
                color = Color.PURPLE;
                break;
            case 6:
                color = Color.MAROON;
                break;
            case 7:
                color = Color.MAGENTA;
                break;
            case 8:
                color = Color.WHITE;
                break;
            default:
                color = Color.GRAY;
                break;
        }
    }
}
