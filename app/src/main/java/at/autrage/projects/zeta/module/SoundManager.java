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

    MediaPlayer m_MediaPlayer;
    SoundPool m_SoundPool;
    HashMap<Integer, Integer> m_ResIdToSoundIdMap;
    int m_LengthToResume;
    float m_BGMVolume;
    float m_SFXVolume;

    public final int MaxSFXStreams = 5;

    private SoundManager() {
        m_BGMVolume = 1f;
        m_SFXVolume = 1f;
    }

    public void initialize(MainActivity mainActivity) {
        m_MainActivity = mainActivity;
        m_MainContext = mainActivity.getApplicationContext();

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
        addSoundToSoundPool(R.raw.sfx_button_pause);
        addSoundToSoundPool(R.raw.sfx_button_resume);
        addSoundToSoundPool(R.raw.sfx_change_weapon);
        addSoundToSoundPool(R.raw.sfx_hit_planet);
        addSoundToSoundPool(R.raw.sfx_hit_rocket);
        addSoundToSoundPool(R.raw.sfx_launch_rocket);
        addSoundToSoundPool(R.raw.sfx_drumhits_new_highscore);
        addSoundToSoundPool(R.raw.sfx_drumhits_next_level);
        addSoundToSoundPool(R.raw.sfx_ending_loose);
        addSoundToSoundPool(R.raw.sfx_ending_win);
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
            try {
                m_MediaPlayer.stop();
                m_MediaPlayer.release();
            } catch (IllegalStateException e) {
                // Do nothing...
            }

            m_MediaPlayer = null;
        }
    }

    public void PauseBGM() {
        if (m_MediaPlayer != null) {
            m_MediaPlayer.pause();
            m_LengthToResume = m_MediaPlayer.getCurrentPosition();
        }
    }

    public void ResumeBGM() {
        if (m_MediaPlayer != null) {
            m_MediaPlayer.seekTo(m_LengthToResume);
            m_MediaPlayer.start();
        }
    }

    public void setBGMVolume(float volume) {
        if (m_MediaPlayer != null) {
            m_MediaPlayer.setVolume(volume, volume);
        }

        m_BGMVolume = volume;
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
        if (m_SFXVolume == 0f) {
            return;
        }

        Integer soundId = m_ResIdToSoundIdMap.get(resId);
        if (soundId == null) {
            Logger.E("Could not play sound effect with resource id " + resId + ", because it was not loaded.");
            return;
        }

        m_SoundPool.play(soundId, m_SFXVolume, m_SFXVolume, 0, 0, 1);
    }

    public void setSFXVolume(float volume) {
        m_SFXVolume = volume;
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
    }
}
