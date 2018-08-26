package com.example.media.listener;

/**
 * @author ldg
 * @date 2018/8/22
 */
public interface IMediaPlayStateListener {

    void onStateError();

    void onStateIdle();

    void onStatePrepare();

    void onStatePrepareAsync();

    void onStatePrepared();

    void onStatePlaying();

    void onStatePaused();

    void onStatePlaybackCompleted();

    void onSeekComplete();

    void onBufferingUpdate(int progress);

    void onInfo(int what, int extra);

    void onProgressUpdate(int progress, long position, long duration);

    void onEnterSecondScreen();

    void onExitSecondScreen();
}
