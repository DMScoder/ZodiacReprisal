package com.zodiac.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.zodiac.Support.Messagable;
import com.zodiac.Support.SoundMaster;
import com.zodiac.UI.ShipGroup;
import com.zodiac.World.GameState;
import com.zodiac.World.Player;
import com.zodiac.World.Starship;
import com.zodiac.ZodiacReprisal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by Immortan on 12/11/2017.
 */
public class GameScreen extends ZodiacScreen implements InputProcessor{

    private static final Logger LOGGER = Logger.getLogger(GameScreen.class.getName());

    private ZodiacReprisal game;
    private int currentLine=0;
    private String[] messages = new String[10];
    private String activeString="";
    private BitmapFont bitmapFont;
    private ArrayList<String> spawnList = new ArrayList<String>();
    private ArrayList<ShipGroup> shipGroups;
    private String displayName="";
    private GameState gameState;
    private boolean typing;
    private int playerCount;
    private ShapeRenderer shapeRenderer;

    private ArrayList<Player> players;
    private int playerID;
    private boolean dragging;
    private int startDragX;
    private int startDragY;
    private int endDragX;
    private int endDragY;

    //For testing only
    public GameScreen(ZodiacReprisal game){
        this.game = game;
        gameState = new GameState(this);
        bitmapFont = new BitmapFont();
        Gdx.input.setInputProcessor(this);
        spawnList = new ArrayList<String>();
        spawnList.add("0>0>0>1>10>0>0>0>0>0>");
        spawnList.add("1>50000>50000>0>1>0>0>0>0>0>");
        spawnList.add("2>50000>0>1>1>0>0>0>0>0>");
        spawnList.add("3>0>50000>0>1>0>0>0>0>0>");
        players = new ArrayList<Player>();
        players.add(new Player(0,"THE PLAYER"));
        players.add(new Player(1,"FAUX PLAYER"));
        players.add(new Player(2,"FAKE PLAYER"));
        players.add(new Player(3,"UNREAL PLAYER"));
        gameState.spawnShips(spawnList,players);
        gameState.setHomePlayer(players.get(0));
        shapeRenderer = new ShapeRenderer();
    }

    public GameScreen(ZodiacReprisal game, int playerCount, ArrayList<ShipGroup> shipGroups){
        this.game = game;
        this.playerCount = playerCount;
        this.shipGroups = shipGroups;
        players = new ArrayList<Player>();

        gameState = new GameState(this);
        bitmapFont = new BitmapFont();
        Gdx.input.setInputProcessor(this);
        SoundMaster.stopAll();
        sendMessage(Messagable.WHO_ARE_WE,"");
        shapeRenderer = new ShapeRenderer();
    }

    private void sendShipList(ArrayList<ShipGroup> shipGroups){
        String message = shipGroups.get(0).getFaction()+">";

        for(int i=0;i<shipGroups.size();i++){
            message+=shipGroups.get(i).getAmount()+">";
        }

        sendMessage(Messagable.SHIP_LIST,message);
    }

    public void checkSingularConditions(){
        if(getSpawnList().size()==playerCount){
            gameState.spawnShips(getSpawnList(),players);
            getSpawnList().clear();
        }
        if(!displayName.equals("")&&shipGroups!=null){
            sendShipList(shipGroups);
            shipGroups = null;
        }
    }

    public void render(float delta){
        GL20 gl = Gdx.gl;
        gl.glClearColor(5/255f,5/255f,22/255f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        drawMessages();
        drawRemainingShips();
        game.getBatch().end();
        gameState.render(delta);
        checkSingularConditions();
        if(dragging)
            drawSelectionBox();
    }

    private void drawRemainingShips(){
        ArrayList<Starship> ships = gameState.getShips();

        for(int i=0;i<players.size();i++){
            Player player = players.get(i);
            player.ships_owned=0;

            for(int j=0;j<ships.size();j++){
                if(ships.get(j).getPlayer()==player)
                    player.ships_owned++;
            }

            bitmapFont.setColor(player.ships_owned == 0 ? new Color(1,1,1,0.3f) : player.getColor());
            bitmapFont.draw(game.getBatch(),player.getName()+" ships remaining: " +player.ships_owned,
                    Gdx.graphics.getWidth()*2/3,Gdx.graphics.getHeight()-(5+i*12));
        }

        bitmapFont.setColor(Color.WHITE);
    }

    private void drawSelectionBox(){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(startDragX,Gdx.graphics.getHeight()-startDragY,endDragX-startDragX,Gdx.graphics.getHeight()-endDragY-(Gdx.graphics.getHeight()-startDragY));
        shapeRenderer.end();
    }

    private synchronized ArrayList<String> getSpawnList(){
        return spawnList;
    }

    @Override
    public void messageReceived(int type, String message) {
        switch (type){
            case Messagable.YOU_ARE:
                System.out.println(message);
                playerID = Integer.valueOf(message.split(">",2)[0]);
                System.out.println(playerID);
                displayName= message.split(">",3)[1];
                System.out.println(displayName);
                String[] playerList = message.split(">",3)[2].split(">",0); //Extract all player names
                System.out.println(Arrays.toString(playerList));

                for(int i=0;i<playerList.length;i+=2){
                    players.add(new Player(Integer.valueOf(playerList[i]),playerList[i+1]));
                    if(Integer.valueOf(playerList[i])==playerID)
                        gameState.setHomePlayer(players.get(players.size()-1));
                }

                break;
            case Messagable.CHAT:
                displayMessage(message);
                break;
            case Messagable.SHIP_LIST:
                getSpawnList().add(message);
                System.out.println("RECEIVED SPAWNLIST");
                break;
            case Messagable.SHIP_ORDER:
                gameState.shipOrder(message);
                break;
            case Messagable.NEW_ENTITY:
                gameState.spawnNeutral(message);
                break;
        }
    }

    @Override
    public void sendMessage(int type, String message) {
        game.sendMessage(type,message);
    }

    private void drawMessages(){
        for(int i=0;i<messages.length;i++){
            if(messages[i]!=null&&!messages[i].equals(""))
                bitmapFont.draw(game.getBatch(),messages[i],5,Gdx.graphics.getHeight()-(5+i*12));
        }

        bitmapFont.draw(game.getBatch(),activeString,5,30);
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
        if(keycode == Input.Keys.BACKSPACE&&typing){
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

            if(typing){
                if(!activeString.equals("")) {
                    sendMessage(Messagable.CHAT, displayName + ": " + activeString);
                    activeString = "";
                    typing=false;
                }
                else
                    typing=false;
            }
            else
                typing=true;
        }

        else
            gameState.keyPressed(keycode);

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(!typing)
            gameState.keyDepressed(keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if(Character.isDigit(character)||Character.isLetter(character)||character==' '
                ||character=='!'||character==','||character=='?'||character=='.'
                ||character=='('||character==')'||character==':')
            if(activeString.length()<50&&typing)
                activeString += character;

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        gameState.clicked(screenX,screenY,button);
        startDragX = screenX;
        startDragY = screenY;
        endDragX = startDragX;
        endDragY = startDragY;

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(dragging)
        {
            dragging = false;
            gameState.boxSelect(startDragX,startDragY,screenX,screenY);
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            dragging = true;
            endDragX = screenX;
            endDragY = screenY;
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        gameState.scrolled(amount,Gdx.input.getX(),Gdx.input.getY());
        return false;
    }
}
