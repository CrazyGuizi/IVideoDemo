package com.example.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.Surface;
import android.widget.Toast;

import com.example.media.abs.AbsMediaPlayer;
import com.example.media.utils.LogUtil;
import com.example.media.view.VideoView;

import java.io.IOException;

/**
 * @author ldg
 * @date 2018/8/22
 */
public class MediaPlayer extends AbsMediaPlayer implements android.media.MediaPlayer.OnErrorListener, android.media.MediaPlayer.OnBufferingUpdateListener, android.media.MediaPlayer.OnSeekCompleteListener, android.media.MediaPlayer.OnVideoSizeChangedListener, android.media.MediaPlayer.OnPreparedListener, android.media.MediaPlayer.OnCompletionListener {

    private final String TAG = getClass().getSimpleName();

    private android.media.MediaPlayer mMediaPlayer;


    public MediaPlayer() {
        mMediaPlayer = new android.media.MediaPlayer();
    }
    @Override
    public void init() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new android.media.MediaPlayer();
            MediaPlayerManager.getInstance().updateState(PlayerState.IDLE);
        }

        // 设置声音
        if (Build.VERSION.SDK_INT > 21) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                    .build();
            mMediaPlayer.setAudioAttributes(attributes);
        } else {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        // 获取播放源
        Object dataSource = getDataSource();
        LogUtil.d(TAG, dataSource + "");
        // 设置播放源
        try {
            if (dataSource != null && dataSource instanceof AssetFileDescriptor) {
                AssetFileDescriptor afd = (AssetFileDescriptor)dataSource;
                    mMediaPlayer.setDataSource(afd.getFileDescriptor(),
                            afd.getStartOffset(),
                            afd.getLength());
            } else {
                LogUtil.d(TAG, "dataSource" + dataSource);
                mMediaPlayer.setDataSource((String) dataSource);
            }
        } catch (IOException e) {
            MediaPlayerManager.getInstance().updateState(PlayerState.ERROR);
            e.printStackTrace();
            LogUtil.d(TAG, "设置源文件出错");
        }

        registerListener();
    }

    // 注册回调
    private void registerListener() {
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
    }

    // 异步准备，当准备好后会回调OnPreparedListener方法
    @Override
    public void prepareAsync() {
        init();
        if (mMediaPlayer != null) {
            mMediaPlayer.prepareAsync();
            MediaPlayerManager.getInstance().updateState(PlayerState.PREPARE_ASYNC);
        }
    }

    // 同步
    @Override
    public void prepare() {
        init();
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.prepare();
                MediaPlayerManager.getInstance().updateState(PlayerState.PREPARE);
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.d(TAG, "prepare 方法出错");
                MediaPlayerManager.getInstance().updateState(PlayerState.ERROR);
            }
        }
    }

    @Override
    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            MediaPlayerManager.getInstance().updateState(PlayerState.PLAYING);
        }
    }

    @Override
    public void paused() {
        if ( mMediaPlayer != null) {
            mMediaPlayer.pause();
            MediaPlayerManager.getInstance().updateState(PlayerState.PAUSE);
        }
    }

    @Override
    public void stop() {
        if (mMediaPlayer != null)
            mMediaPlayer.stop();
    }

    @Override
    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void seekTo(long position) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo((int) position);
        }
    }

    @Override
    public void looping(boolean isLooping) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setLooping(isLooping);
        }
    }

    @Override
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }
    }

    @Override
    public long getCurrentPosition() {
        if (mMediaPlayer != null)
            return mMediaPlayer.getCurrentPosition();
        return 0;
    }

    @Override
    public long getDuration() {
        if (mMediaPlayer != null)
            return mMediaPlayer.getDuration();
        return 0;
    }

    @Override
    public void setSurface(Surface surface) {
        if (mMediaPlayer != null)
            mMediaPlayer.setSurface(surface);
    }

    @Override
    public void setMute(boolean isMute) {
        if (isMute)
            closeVolume();
        else
            openVolume();
    }

    private void closeVolume() {
        if (mMediaPlayer != null) {
            mMediaPlayer.setVolume(0, 0);
        }
    }

    private void openVolume() {
        if (mMediaPlayer != null) {
            VideoView videoView = MediaPlayerManager.getInstance().getCurrentVideoView();
            if (videoView != null) {
                Context context = videoView.getContext();
                AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                int streamVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setVolume(streamVolume, streamVolume);
            }
        }
    }


    @Override
    public void onPrepared(android.media.MediaPlayer mp) {
        if (mMediaPlayer != null) {
            MediaPlayerManager.getInstance().updateState(PlayerState.PREPARED);
            LogUtil.d(TAG, "准备停当，开始播放");
        }
    }

    // 播放出错
    @Override
    public boolean onError(android.media.MediaPlayer mp, int what, int extra) {
        MediaPlayerManager.getInstance().updateState(PlayerState.ERROR);
        return true;
    }

    @Override
    public void onBufferingUpdate(android.media.MediaPlayer mp, int percent) {
        LogUtil.d(TAG, "滴滴滴" + percent);
    }

    @Override
    public void onSeekComplete(android.media.MediaPlayer mp) {
    }

    @Override
    public void onVideoSizeChanged(android.media.MediaPlayer mp, int width, int height) {
        MediaPlayerManager.getInstance().onVideoSizeChanged(width, height);
    }

    @Override
    public void onCompletion(android.media.MediaPlayer mp) {
        LogUtil.d(TAG, "播放完毕");
        MediaPlayerManager.getInstance().updateState(PlayerState.PLAYBACK_COMPLETED);
    }

}
