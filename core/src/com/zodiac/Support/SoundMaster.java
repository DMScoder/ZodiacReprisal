package com.zodiac.Support;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Created by Immortan on 12/10/2017.
 */
public class SoundMaster {

    public static final int AMBIENT = 0;
    public static final int PHOTONIC = 1;
    public static final int FEDERATION = 2;

    private static Music[][] musicList = new Music[3][10];

    public static void initialize(){
        musicList[AMBIENT][0] = Gdx.audio.newMusic(Gdx.files.internal("Music/ambient.ogg"));
        musicList[PHOTONIC][0] = Gdx.audio.newMusic(Gdx.files.internal("Music/EndOfLine.mp3"));
        musicList[FEDERATION][0] = Gdx.audio.newMusic(Gdx.files.internal("Music/ArtOfWar.mp3"));
        musicList[FEDERATION][1] = Gdx.audio.newMusic(Gdx.files.internal("Music/prepareforwar.mp3"));
    }

    public static void play(int list, int selection){
        for(int i=0;i<musicList.length;i++){
            for(int j=0;j<musicList.length;j++)
                if(musicList[i][j]!=null)
                    musicList[i][j].pause();
        }

        if(musicList[list][selection].getPosition()==0)
            musicList[list][selection].setPosition(1);
        //musicList[list][selection].play();
        musicList[list][selection].setLooping(true);
    }

    public static void stopAll(){
        for(int i=0;i<musicList.length;i++){
            for(int j=0;j<musicList.length;j++)
                if(musicList[i][j]!=null)
                    musicList[i][j].stop();
        }
    }
}
