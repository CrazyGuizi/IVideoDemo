package com.example.media.abs;

import android.view.Surface;

/**
 * mediaPlayer的抽象类，提供视频播放的接口
 *
 * @author ldg
 * @date 2018/8/22
 */
public abstract class AbsMediaPlayer {

    private Object mDataSource;

    // 初始化工作
    protected abstract void init();

    // 准备，异步
    public abstract void prepareAsync();

    // 准备
    public abstract void prepare();

    // 开始播放
    public abstract void start();

    // 暂停
    public abstract void paused();

    // 停止
    public abstract void stop();

    // 重置
    public abstract void reset();



    public abstract boolean isPlaying();
    // 指定到
    public abstract void seekTo(long position);

    // 循环播放
    public abstract void looping(boolean isLooping);

    // 释放资源
    public abstract void release();

    // 获取当前进度
    public abstract long getCurrentPosition();

    // 总时长
    public abstract long getDuration();

    // 设置播放内容的容器
    public abstract void setSurface(Surface surface);

    // 设置静音
    public abstract void setMute(boolean isMute);

    // 设置播放源
    public  void setDataSource(Object dataSource) {
        this.mDataSource = dataSource;
    }

    public Object getDataSource() {
        return mDataSource;
    }
}
