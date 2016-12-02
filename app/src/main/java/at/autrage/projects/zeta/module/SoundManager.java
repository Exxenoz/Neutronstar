package at.autrage.projects.zeta.module;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.activity.MainActivity;

public class SoundManager implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, SoundPool.OnLoadCompleteListener {
    private static SoundManager m_Instance = new SoundManager();

    public static SoundManager getInstance() {
        return m_Instance;
    }

    MainActivity m_MainActivity;
    Context m_MainContext;

    AudioManager m_AudioManager;
    MediaPlayer m_MediaPlayer;
    SoundPool m_SoundPool;
    boolean m_SoundPoolLoaded;
    HashMap<Integer, Integer> m_ResIdToSoundIdMap;

    public final int MaxSFXStreams = 5;

    private SoundManager() {
    }

    public void initialize(MainActivity mainActivity) {
        m_MainActivity = mainActivity;
        m_MainContext = mainActivity.getApplicationContext();

        m_AudioManager = (AudioManager)m_MainContext.getSystemService(MainActivity.AUDIO_SERVICE);

        // Initialize SoundPool (for sound effects):
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            m_SoundPool = createNewSoundpool();
        }
        else {
            m_SoundPool = createOldSoundpool();
        }

        m_SoundPool.setOnLoadCompleteListener(this);

        loadSoundPool();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private SoundPool createNewSoundpool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        return new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(MaxSFXStreams)
                .build();
    }

    @SuppressWarnings("deprecation") // SupressWarnings should not be used, still in this case it is needed
    private SoundPool createOldSoundpool() {
        return new SoundPool(MaxSFXStreams, AudioManager.STREAM_MUSIC, 0);
    }

    private void addSoundToSoundPool(int resId) {
        m_ResIdToSoundIdMap.put(resId, m_SoundPool.load(m_MainContext, resId, 1));
    }

    private void loadSoundPool() {
        m_ResIdToSoundIdMap = new HashMap<>();

        addSoundToSoundPool(R.raw.sfx_button_back);
        addSoundToSoundPool(R.raw.sfx_button_pick);
        addSoundToSoundPool(R.raw.sfx_button_select);
    }

    public void StartBGM(int resId, boolean looping) {
        StopBGM();

        m_MediaPlayer = MediaPlayer.create(m_MainContext, resId);
        m_MediaPlayer.setLooping(looping);
        m_MediaPlayer.setOnPreparedListener(this);
        m_MediaPlayer.setOnCompletionListener(this);
    }

    public void StopBGM() {
        if (m_MediaPlayer != null) {
            m_MediaPlayer.stop();
            m_MediaPlayer.release();
            m_MediaPlayer = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp != null) {
            mp.start();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mp != null) {
            mp.release();
        }
    }

    public void PlaySFX(int resId) {
        Integer soundId = m_ResIdToSoundIdMap.get(resId);
        if (soundId == null) {
            Logger.E("Could not play sound effect with resource id " + resId + ", because it was not loaded.");
            return;
        }

        if (!m_SoundPoolLoaded) {
            Logger.E("Could not play sound effect with resource id " + resId + ", because sound pool is not fully loaded yet.");
            return;
        }

        m_SoundPool.play(soundId, 1, 1, 0, 0, 1);
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        m_SoundPoolLoaded = true;
    }
}
