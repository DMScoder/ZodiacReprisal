package com.zodiac.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.zodiac.DATA.Constants;
import com.zodiac.Support.AM;
import com.zodiac.Support.Messagable;
import com.zodiac.Support.SoundMaster;
import com.zodiac.UI.PlayerGrouping;
import com.zodiac.UI.PressableButton;
import com.zodiac.UI.ShipGroup;
import com.zodiac.UI.StringContainer;
import com.zodiac.ZodiacReprisal;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Immortan on 12/9/2017.
 */
public class Menu extends ZodiacScreen implements InputProcessor {

    ZodiacReprisal game;
    private int currentLine=0;
    private String[] messages = new String[10];
    private String activeString="";
    private BitmapFont bitmapFont = new BitmapFont();
    private String displayName = "";

    private PressableButton photonicIcon;
    private PressableButton federationIcon;
    private PressableButton readyButton;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private ArrayList<StringContainer> stringContainers = new ArrayList<StringContainer>();
    private ArrayList<ShipGroup> shipGroups = new ArrayList<ShipGroup>();
    private ArrayList<PlayerGrouping> playerGroupings = new ArrayList<PlayerGrouping>();

    private float width = Gdx.graphics.getWidth();
    private float height = Gdx.graphics.getHeight();
    private int requisition = 1000;

    private boolean ready;
    private boolean nameSelected;
    private boolean factionSelected;
    private boolean start;
    private int playerCount;

    public Menu(ZodiacReprisal game){
        this.game = game;

        Gdx.input.setInputProcessor(this);
        displayMessage("Enter in your display name");
        SoundMaster.play(SoundMaster.AMBIENT,0);
    }

    public void setupUI(){
        photonicIcon = new PressableButton(AM.fIcons[Constants.PHOTONICS],width/20,height/6,height/6,height/6);
        federationIcon = new PressableButton(AM.fIcons[Constants.FEDERATION],width/20,height/3+height/12,height/6,height/6);
        federationIcon.setToggle(false);
        photonicIcon.setToggle(false);
        stringContainers.add(new StringContainer("Select Faction",width/20,height/3*2, Align.center,height/6));
        readyButton = new PressableButton(AM.utilIcons[AM.CHECK],AM.utilIcons[AM.X],width-width/20-height/8,height-width/20-height/8,height/8,height/8);
        readyButton.setToggle(false);
    }

    private void updatePlayerGroups(String message){
        String details[] = message.split(">",0);

        if(details.length%3!=0)
            return;

        playerGroupings.clear();

        int counter = 1;
        for(int i=0;i<details.length;i+=3){
            playerGroupings.add(new PlayerGrouping(details[i],Integer.valueOf(details[i+1]),Boolean.valueOf(details[i+2]),counter));
            counter++;
        }
    }

    private void setupShipGroups(int faction){

        int counter=0;
        shipGroups.clear();
        for(int i=0;i<4;i++){
            for(int j=0;j<2;j++){
                if(counter==7)
                    continue;
                shipGroups.add(new ShipGroup(width/20+width/8+i*height/3,j*height/3+25,height/3,height/3,faction,counter));
                counter++;
            }
        }
    }

    private void update(){
        if(start)
            game.startGame(playerCount,shipGroups);

        if(Gdx.input.justTouched()){
            float x = Gdx.input.getX();
            float y = height - Gdx.input.getY();

            if(nameSelected) {
                if (federationIcon.contains(x, y)) {
                    federationIcon.setToggle(true);
                    photonicIcon.setToggle(false);
                    setupShipGroups(Constants.FEDERATION);
                    SoundMaster.play(SoundMaster.FEDERATION,1);
                    sendMessage(Messagable.FACTION_SELECT, Constants.FEDERATION + "");
                    setupShipGroups(Constants.FEDERATION);
                    factionSelected=true;
                    return;
                } else if (photonicIcon.contains(x, y)) {
                    photonicIcon.setToggle(true);
                    federationIcon.setToggle(false);
                    setupShipGroups(Constants.PHOTONICS);
                    setupShipGroups(Constants.PHOTONICS);
                    SoundMaster.play(SoundMaster.PHOTONIC,0);
                    sendMessage(Messagable.FACTION_SELECT, Constants.PHOTONICS + "");
                    factionSelected=true;
                    return;
                }
            }

            if(factionSelected){
                if(readyButton.contains(x,y)){
                    if(!atLeastOneShipChosen()){
                        displayMessage("You must choose at least one ship!");
                    }
                    else{
                        ready=!ready;
                        readyButton.setToggle(ready);
                        game.sendMessage(Messagable.READY,"");
                    }
                }
            }

            for(int i=0;i<shipGroups.size();i++){
                requisition += shipGroups.get(i).checkClick(x,y, requisition);
            }
        }
    }

    private void drawMessages(){
        for(int i=0;i<messages.length;i++){
            if(messages[i]!=null&&!messages[i].equals(""))
                bitmapFont.draw(game.getBatch(),messages[i],5,Gdx.graphics.getHeight()-(5+i*12));
        }

        bitmapFont.draw(game.getBatch(),activeString,5,30);
    }

    private void draw(){
        GL20 gl = Gdx.gl;
        gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for(int i=0;i<shipGroups.size();i++){
            shipGroups.get(i).drawShapeRender(shapeRenderer);
        }
        shapeRenderer.end();

        game.getBatch().begin();
        drawMessages();

        if(nameSelected){
            federationIcon.draw(game.getBatch());
            photonicIcon.draw(game.getBatch());
        }

        for(int i=0;i<shipGroups.size();i++){
            shipGroups.get(i).drawBatch(game.getBatch(), bitmapFont);
        }

        for(int i=0;i<stringContainers.size();i++){
            stringContainers.get(i).draw(game.getBatch(),bitmapFont);
        }

        for(int i=0;i<playerGroupings.size();i++){
            playerGroupings.get(i).draw(game.getBatch(),bitmapFont);
        }

        if(factionSelected) {
            readyButton.draw(game.getBatch());
            bitmapFont.draw(game.getBatch(), "Points remaining: " + requisition, width - height / 4.5f, height - width / 20 - height / 7);
        }

        game.getBatch().end();
    }

    public void messageReceived(int type, String message){
        //System.out.println("MENU: "+message);
        switch (type){
            case Messagable.CHAT:
                displayMessage(message);
                break;
            case Messagable.NAME:
                displayMessage(message+" has joined the lobby");
                break;
            case Messagable.DISCONNECT:
                displayMessage(message+" has left the lobby");
                break;
            case Messagable.PLAYER_LIST:
                updatePlayerGroups(message);
                break;
            case Messagable.START:
                start = true;
                playerCount = Integer.valueOf(message);
                break;
        }
    }

    public void render(float delta){
        update();
        draw();
    }

    private void displayMessage(String message){
        if(currentLine>messages.length-1){
            for(int i=0;i<messages.length-1;i++){
                messages[i] = messages[i+1];
            }
            messages[messages.length-1] = message;
        }
        else{
            messages[currentLine] = message;
            currentLine++;
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACKSPACE){
            if(activeString.length()==0)
                return true;

            char tempChars[] = activeString.toCharArray();
            char newChars[] = new char[tempChars.length-1];

            for(int i=0;i<newChars.length;i++){
                newChars[i] = tempChars[i];
            }

            activeString = new String(newChars);
        }

        else if(keycode == Input.Keys.ENTER){
            if(!activeString.equals("")) {
                if(displayName.equals("")){
                    displayName = activeString;
                    sendMessage(Messagable.NAME,displayName);
                    nameSelected = true;
                    setupUI();
                }
                else{
                    sendMessage(Messagable.CHAT,displayName+": "+activeString);
                }
            }

            activeString = "";
        }

        return true;
    }

    public void sendMessage(int type, String message){
        game.sendMessage(type,message);
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if(Character.isDigit(character)||Character.isLetter(character)||character==' '
                ||character=='!'||character==','||character=='?'||character=='.'
                ||character=='('||character==')'||character==':'&&activeString.length()<50)
            activeString += character;

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private boolean atLeastOneShipChosen(){
        for(int i=0;i<shipGroups.size();i++)
            if(shipGroups.get(i).getAmount()!=0)
                return true;
        return false;
    }
}
