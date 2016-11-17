package com.jeffrey.android.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {

    private static final String TAG = "BeatBox"; //TAG i use it when i want to show message in logcat [tool in eclipse you can watch your app messages when it is running] by saying String TAG= yourclassname.class.getsimpleName();

    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5; //CREATING A SOUNDPOOL, PAGE 339

    private AssetManager mAssets;  //Provides access to an application's raw asset files; see Resources for the way most applications will want to retrieve their resource data.
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;  //CREATING A SOUNDPOOL, PAGE 339

    public BeatBox(Context context){
        mAssets = context.getAssets();
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);//CREATING A SOUNDPOOL, PAGE 339
        loadSounds();           // IN DE 1E PARAMETER GEEFT AANTAL SOUNDS AAN DIE TEGELIJKTIJD KUNNEN WORDEN AFGESPEELD
    }                           //ALS 2E PARAMETER GEEF JE AAN WEL AUDIO STREAM SOUNDPOOL GAAT GEBRUIKEN, PAGE 340
                                //DE 3E PARAMETER IS DE KWALITEIT VAN DE SAMPLE RATE CONVERTER... STANDAARD 0

    public void play(Sound sound){
        Integer soundId = sound.getSoundId();
        if (soundId == null){
            return;
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f ); //PAGE 342, PARAMETERS SOUND
    }

    public void release(){          //UNLOADING SOUNDS PAGE 343
        mSoundPool.release();;
    }

    private void loadSounds(){
        String[] soundNames;
        try {
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + soundNames.length + " sounds");
        }catch (IOException ioe){
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }

        for (String filename : soundNames){
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            }catch (IOException ioe) {
                Log.e(TAG, "Could not load sound " + filename, ioe);
            }
        }
    }

    private void load(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

    public List<Sound> getSounds(){
        return mSounds;
    }

}
