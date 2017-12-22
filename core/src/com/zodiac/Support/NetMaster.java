package com.zodiac.Support;

import com.zodiac.ZodiacReprisal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Immortan on 12/9/2017.
 */
public class NetMaster implements Runnable{

    private static final Logger LOGGER = Logger.getLogger(NetMaster.class.getName());

    ZodiacReprisal game;

    //Static addresses
    //public static final String SERVER_ADDRESS = "54.218.16.42";
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 60000;

    //IO
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket = null;
    private static final String GREETING = "WITNESS ME!";
    private static final String CORRECT_RESPONSE = "WITNESSED!";

    //Thread specific
    private boolean active = false;
    private Thread listeningThread;
    //private ArrayList <String> buffer = new ArrayList<String>();
    //private ArrayList <Integer>bufferType = new ArrayList<Integer>();

    public NetMaster(ZodiacReprisal game){
        listeningThread = new Thread(this);
        listeningThread.start();
        this.game = game;
    }

    @Override
    public void run() {
        //Start connection
        try{
            LOGGER.log(Level.INFO,"Connecting to server...");

            socket = new Socket(SERVER_ADDRESS,PORT);
            out = new PrintWriter(socket.getOutputStream(),true);
            out.println(GREETING);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            if(in.readLine().equals(CORRECT_RESPONSE)){
                setActive(true);
                LOGGER.log(Level.INFO,"Connection successful");
            }
        }catch (IOException e){
            e.printStackTrace();
            LOGGER.log(Level.INFO,"Connection failed :(");
        }

        //For receiving messages
        try {
            while (active) {
                String message = in.readLine();

                if(message.equals("TESTING"))
                    sendMessage(Messagable.TEST,"123");

                if(!message.equals("")&&!message.equals("TESTING")){
                    LOGGER.log(Level.INFO, "Received message "+message);
                    game.messageReceived(Integer.valueOf(message.split(">",2)[0]),message.split(">",2)[1]);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            LOGGER.log(Level.INFO,"Connection trouble");
        }

        //End connection
        try{
            if(socket!=null)
                socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public synchronized void setActive(boolean b){active = b;}

    public synchronized void sendMessage(int type, String s){
        if(out!=null)
            out.println(type+">"+s);
    }

    //public synchronized boolean isMessageAvailable(){return messageAvailable;}

    /*public synchronized int getNewMessageType(){
        if(bufferType.size()==1)
            messageAvailable = false;

        return bufferType.size()>0?bufferType.remove(0):-1;
    }

    public synchronized String getNewMessage(){
        if(buffer.size()==1)
            messageAvailable = false;

        return buffer.size()>0?buffer.remove(0):null;
    }*/
}
