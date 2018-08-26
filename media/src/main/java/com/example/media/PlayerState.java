package com.example.media;

/**
 * 播放的状态
 *
 * @author ldg
 * @date 2018/8/22
 */
public enum PlayerState {
    IDLE,
    ERROR,
    PREPARE_ASYNC,
    PREPARE,
    PREPARED, //已经准备好了，可以进入播放状态
    PLAYING,
    PAUSE,
    PLAYBACK_COMPLETED
}
