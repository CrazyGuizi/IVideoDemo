package com.example.media;

import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.example.media.abs.AbsMediaPlayer;
import com.example.media.utils.LogUtil;
import com.example.media.view.ResizeTextureView;
import com.example.media.view.VideoView;
import com.example.media.view.WindowType;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 用于控制音视频的操作
 *
 * @author ldg
 * @date 2018/8/22
 */
public class MediaPlayerManager implements TextureView.SurfaceTextureListener {

    private final String TAG = getClass().getSimpleName();
    private PlayerState mPlayerState;
    private WindowType mWindowType = WindowType.NORMAL;

    private Object mOldData = null;
    private VideoView mVideoView;
    private ResizeTextureView mTextureView;
    //    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;

    private AbsMediaPlayer mMediaPlayer;

    private Timer mTimer;
    private UpdateProgressTask mUpdateProgressTask;

    private MediaPlayerManager() {
//        mMediaPlayer = new MediaPlayer();
    }

    public static MediaPlayerManager getInstance() {
        return Holder.INSTANCE;
    }


    private static class Holder {
        private static MediaPlayerManager INSTANCE = new MediaPlayerManager();
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        LogUtil.d(TAG, "onSurfaceTextureAvailable");
        if (mMediaPlayer != null) {
            if (mOldData != mVideoView.getDataSource())
                mMediaPlayer.setDataSource(mVideoView.getDataSource());
                if (mPlayerState != PlayerState.PAUSE) {
                    mMediaPlayer.prepareAsync();
                    LogUtil.d(TAG, "准备");
                } else
                    start();
                if (mSurface == null)
                    createSurface();
                mMediaPlayer.setSurface(mSurface);
                mOldData = mVideoView.getDataSource();
            }
    }

    public void createSurface() {
        if (mSurface == null && mTextureView != null) {
            mSurface = new Surface(mTextureView.getSurfaceTexture());
            LogUtil.d(TAG, "创建mSurface");
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        LogUtil.d(TAG, "method is invoked: onSurfaceTextureDestroyed");
        mSurface = null;
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public void init(VideoView videoView) {
        LogUtil.d(TAG, "init");
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }

        mVideoView = videoView;
        mTextureView = videoView.getVideo();
        mTextureView.setSurfaceTextureListener(this);
    }

    public void changeDataSource(Object object) {
        if (mVideoView != null) {
            mMediaPlayer.reset();
            mVideoView.setDataSource(object);
            if (mMediaPlayer != null) {
                if (mOldData != mVideoView.getDataSource())
                    mMediaPlayer.setDataSource(mVideoView.getDataSource());
                if (mPlayerState != PlayerState.PAUSE) {
                    mMediaPlayer.prepareAsync();
                    LogUtil.d(TAG, "准备");
                } else
                    start();
                if (mSurface == null)
                    createSurface();
                mMediaPlayer.setSurface(mSurface);
                mOldData = mVideoView.getDataSource();
            }

        }
    }

    // 暂停
    public void pause() {
        mMediaPlayer.paused();
        updateState(PlayerState.PAUSE);
    }

    public long getDuration() {
        if (mMediaPlayer != null)
            return mMediaPlayer.getDuration();
        return 0;
    }

    public long getCurrentPosition() {
        if (mMediaPlayer != null)
            return mMediaPlayer.getCurrentPosition();
        return 0;
    }

    // 返回
    public void back() {

    }

    public WindowType getWindowType() {
        return mWindowType;
    }

    // 指定到
    public void seekTo(int i) {
        mMediaPlayer.seekTo(i);
    }


    // 开始播放
    public void start() {
        mMediaPlayer.start();
        updateState(PlayerState.PLAYING);
        LogUtil.d(TAG, "start");
    }

    // 更新播放状态
    public void updateState(PlayerState state) {
        mPlayerState = state;
        mVideoView.getPanel().notifyStateChange();
        switch (state) {
            case PLAYING:
            case PAUSE:
                startUpdateProgress();
                break;
            case IDLE:
            case ERROR:
                cancelUpdateProgress();
                break;
            case PLAYBACK_COMPLETED:
//                mVideoView.getPanel().notifyStateChange();
                cancelUpdateProgress();
                break;
        }
    }

    // 取消更新进度条
    private void cancelUpdateProgress() {
        if (mTimer != null) {
            mTimer.cancel();
        }

        if (mUpdateProgressTask != null) {
            mUpdateProgressTask.cancel();
        }
    }

    // 开始更新进度条
    public void startUpdateProgress() {
        cancelUpdateProgress();
        mUpdateProgressTask = new UpdateProgressTask();
        mTimer = new Timer();
        mTimer.schedule(mUpdateProgressTask, 0, 1000);
    }

    public void setWindowType(WindowType windowType) {
        mWindowType = windowType;
    }

    // 视频窗口改变
    public void onVideoSizeChanged(int width, int height) {

    }

    public PlayerState getPlayerState() {
        return mPlayerState;
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public VideoView getCurrentVideoView() {

        return mVideoView;
    }

    public void release() {
        cancelUpdateProgress();

        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.setDataSource(null);
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        if (mPlayerState != null) {
            mPlayerState = null;
        }

        if (mTextureView != null) {
            mTextureView = null;
        }

        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }

        mOldData = null;
    }

    class UpdateProgressTask extends TimerTask {

        @Override
        public void run() {
            // 在暂停的时候如果拖动进度条需要更新进度，所以暂停状态下也是需要的
            if (mPlayerState == PlayerState.PLAYING ||
                    mPlayerState == PlayerState.PAUSE ||
                    mPlayerState == PlayerState.PLAYBACK_COMPLETED) {
                long position = getCurrentPosition();
                long duration = getDuration();
                int progress = (int) (position * 100 / (duration == 0 ? 1 : duration));
                mVideoView.getPanel().onProgressUpdate(progress, position, duration);
            }
        }
    }
}
