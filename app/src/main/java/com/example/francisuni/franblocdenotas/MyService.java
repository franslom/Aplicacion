package com.example.francisuni.franblocdenotas;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service implements MediaPlayer.OnPreparedListener,MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener{
    public MyService() {
    }
    private boolean mMusicBound = false;
    private MediaPlayer reproductor;
    private final IBinder musciBind = new MusicBinder();

    public class MusicBinder extends Binder {
        public MyService getService(){
            return MyService.this;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //reproductor = new MediaPlayer();
        //playMusic();

    }

    public void playMusic(){
        //reproductor = new MediaPlayer();
        reproductor=MediaPlayer.create(this,R.raw.udo);
        reproductor.setLooping(true);
        reproductor.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }
}
