package com.zodiac.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.zodiac.DATA.Constants;
import com.zodiac.Screens.GameScreen;
import com.zodiac.Support.Messagable;
import com.zodiac.Support.NetMaster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Immortan on 12/11/2017.
 */
public class GameState{

    private static final Logger LOGGER = Logger.getLogger(GameState.class.getName());

    private GameRender gameRender;
    private Player homePlayer;
    private ArrayList<Player> players;
    private OrthographicCamera camera;
    private ArrayList<Starship> starships;
    ArrayList<Starship> selected = new ArrayList<Starship>();
    ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();

    private GameScreen gameScreen;
    private float cameraSpeed=500;
    private boolean scrollUp;
    private boolean scrollDown;
    private boolean scrollLeft;
    private boolean scrollRight;


    public GameState(GameScreen gameScreen){
        this.gameScreen = gameScreen;
        gameRender = new GameRender(this);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(0,0,0);
        camera.zoom = 2;
        starships = new ArrayList<Starship>();
        players = new ArrayList<Player>();
        powerUps.add(new PowerUp(0,0, Constants.NEW_SHIP,270,250));
    }

    public void setHomePlayer(Player player){
        this.homePlayer = player;
    }

    public synchronized void createShips(Player player, int faction, int startX, int startY, String ships[]){
        LOGGER.log(Level.INFO,"PLAYER "+player.getName()+" FACTION "+faction+" SHIPS "+ Arrays.toString(ships));

        if(homePlayer == player){
            camera.position.set((float)startX,(float)startY,0);
        }

        int shipID=0;

        for(int i=0;i<ships.length;i++){
            for(int j=0;j<Integer.valueOf(ships[i]);j++) {
                starships.add(new Starship(player, faction, startX + shipID* 500, startY, i, shipID));
                shipID++;
            }
        }
    }

    public void update(float delta){
        camera.translate((scrollLeft?-cameraSpeed*camera.zoom*delta:0) + (scrollRight?cameraSpeed*camera.zoom*delta:0),
                (scrollDown?-cameraSpeed*camera.zoom*delta:0) + (scrollUp?cameraSpeed*camera.zoom*delta:0));
        camera.update();

        for(int i=0;i<starships.size();i++){
            starships.get(i).updatePosition(delta);
        }
        for(int i=0;i<powerUps.size();i++){
            powerUps.get(i).updatePosition(delta);
        }
    }

    public synchronized void spawnNeutral(String string){
        String[] parts = string.split(">");

        powerUps.add(new PowerUp(
                Float.valueOf(parts[1]),
                Float.valueOf(parts[2]),
                Integer.valueOf(parts[0]),
                Integer.valueOf(parts[3]),
                Integer.valueOf(parts[4])));
    }

    public synchronized void spawnShips(ArrayList<String> spawnLists, ArrayList<Player> players){
        this.players = players;

        System.out.println("CREATING SHIPS");
        for(int i=0;i<spawnLists.size();i++) {
            String list = spawnLists.get(i);
            int playerID = Integer.valueOf(list.split(">", 2)[0]);
            int startX = Integer.valueOf(list.split(">", 3)[1]);
            int startY = Integer.valueOf(list.split(">", 4)[2]);
            int faction = Integer.valueOf(list.split(">", 5)[3]);
            String ships = list.split(">", 6)[4];
            createShips(getPlayerByID(players,playerID), faction, startX, startY, ships.split(">", 0));
        }
    }

    public void boxSelect(int startX, int startY, int endX, int endY) {
        Vector3 start = new Vector3(startX,startY,0);
        Vector3 end = new Vector3(endX,endY,0);

        camera.unproject(start);
        camera.unproject(end);

        Polygon rPoly = new Polygon(new float[] { 0, 0, end.x-start.x, 0, end.x-start.x,
                end.y-start.y, 0, end.y-start.y });

        rPoly.setPosition(start.x, start.y);

        if(!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
            selected.clear();

        for(int i = 0; i<starships.size(); i++) {
            Starship unit = (starships.get(i));

            if(Intersector.overlapConvexPolygons(unit.getPolygon(),rPoly)&&unit.getPlayer()==homePlayer)
            {
                selected.add(unit);
            }
        }
    }

    private void selectionClick(Vector2 vector2){
        if(!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
            selected.clear();

        for(int i=0;i<starships.size();i++){
            if(starships.get(i).getPlayer()==homePlayer&&starships.get(i).getPolygon().contains(vector2)){
                selected.add(starships.get(i));
                break;
            }
        }
    }

    private void commandClick(Vector2 vector2){
        //If nothing selected nothing to order
        if(selected.size()==0)
            return;

        //Calculate offset for multiple ships
        float centerX=0,centerY=0;

        for(int i=0;i<selected.size();i++){
            centerX += selected.get(i).getX();
            centerY += selected.get(i).getY();
        }

        centerX = centerX / selected.size();
        centerY = centerY / selected.size();

        for(int i=0;i<selected.size();i++){
            gameScreen.sendMessage(Messagable.SHIP_ORDER,Messagable.ORDER_MOVE+">"
                    +homePlayer.getID()+">"+selected.get(i).getID()+">"
                    +(vector2.x - centerX + selected.get(i).getX())+">"
                    +(vector2.y - centerY + selected.get(i).getY()));
            /*selected.get(i).setMoveOrder(vector2.x - centerX + selected.get(i).getX(),
                    vector2.y - centerY + selected.get(i).getY());*/
        }
    }

    public void shipOrder(String message){

        int order = Integer.valueOf(message.split(">",2)[0]);
        Player player = getPlayerByID(players,Integer.valueOf(message.split(">",3)[1]));
        int shipID = Integer.valueOf(message.split(">",4)[2]);
        float locationX = Float.valueOf(message.split(">",5)[3]);
        float locationY = Float.valueOf(message.split(">",6)[4]);

        for(int i=0;i<starships.size();i++){
            Starship starship = starships.get(i);
            if(starship.getPlayer()==player&&starship.getID()==shipID) {
                starship.setMoveOrder(locationX, locationY);
                break;
            }
        }
    }

    public void clicked(int x, int y, int button) {
        Vector3 vector3 = new Vector3(x,y,0);
        camera.unproject(vector3);
        Vector2 vector2 = new Vector2(vector3.x,vector3.y);

        if(button==Input.Buttons.LEFT)
            selectionClick(vector2);

        if(button==Input.Buttons.RIGHT)
            commandClick(vector2);
    }

    public void scrolled(int n, int x, int y){
        float amount;
        if(n<0) {
            amount = .9f;
        }
        else
            amount = 1.1f;

        float oldZoom = camera.zoom;

        camera.zoom*=amount;

        if(camera.zoom<2) {
            camera.zoom = 2;
            return;
        }
        if(camera.zoom>1000) {
            camera.zoom = 1000;
            return;
        }
        if(n>0)
            return;

        Vector3 translateMouse = camera.unproject(new Vector3(x,y,0));

        camera.position.x += (translateMouse.x - camera.position.x) / (oldZoom-camera.zoom) * camera.zoom>50?.75f:.9f;
        camera.position.y += (translateMouse.y - camera.position.y) / (oldZoom-camera.zoom) * camera.zoom>50?.75f:.9f;

    }

    public void render(float delta){
        update(delta);
        gameRender.draw(camera);
    }

    public void keyPressed(int keyCode){
        if(keyCode== Input.Keys.A)
            scrollLeft = true;
        if(keyCode== Input.Keys.S)
            scrollDown = true;
        if(keyCode== Input.Keys.D)
            scrollRight = true;
        if(keyCode== Input.Keys.W)
            scrollUp = true;
    }

    public void keyDepressed(int keyCode){
        if(keyCode== Input.Keys.A)
            scrollLeft = false;
        if(keyCode== Input.Keys.S)
            scrollDown = false;
        if(keyCode== Input.Keys.D)
            scrollRight = false;
        if(keyCode== Input.Keys.W)
            scrollUp = false;
    }

    //Because player ids do no not necessarily correspond to their location in an array
    public static Player getPlayerByID(ArrayList<Player> players, int targetID){
        for(int i=0;i<players.size();i++){
            if(players.get(i).getID()==targetID)
                return players.get(i);
        }

        return null;
    }
    public ArrayList<PowerUp> getPowerups(){
        return powerUps;
    }

    public ArrayList<Starship> getShips(){
        return starships;
    }


    public ArrayList<Starship> getSelected(){
        return selected;
    }

}
