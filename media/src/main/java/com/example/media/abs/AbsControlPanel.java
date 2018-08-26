package com.example.media.abs;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.media.MediaPlayerManager;
import com.example.media.listener.IMediaPlayStateListener;
import com.example.media.view.VideoView;

/**
 * 控制视频播放的面板
 *
 * @author ldg
 * @date 2018/8/22
 */
public abstract class AbsControlPanel extends FrameLayout implements IMediaPlayStateListener {

    protected VideoView mTarget;

    public AbsControlPanel(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public AbsControlPanel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AbsControlPanel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AbsControlPanel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public void setTarget(VideoView target) {
        mTarget = target;
    }

    public VideoView getTarget() {
        return mTarget;
    }

    // 显示UI
    protected void showUI(View... views) {
            for (View v : views) {
                if (v != null)
                v.setVisibility(View.VISIBLE);
            }

    }

    // 隐藏UI
    protected void hideUI(View... views) {
        if (views == null) return;
            for (View v : views) {
                if (v != null)
                v.setVisibility(View.GONE);
            }
    }


    // 控制面板的布局文件
    protected abstract int getResId();

    protected void initView(Context context) {
        View.inflate(context, getResId(), this);
    }

    // 更改状态
    public void notifyStateChange() {
        switch (MediaPlayerManager.getInstance().getPlayerState()) {
            case IDLE:
                onStateIdle();
                break;
            case ERROR:
                onStateError();
                break;
            case PREPARE:
                onStatePrepare();
                break;
            case PREPARE_ASYNC:
                onStatePrepareAsync();
                break;
            case PREPARED:
                onStatePrepared();
                break;
            case PLAYING:
                onStatePlaying();
                break;
            case PAUSE:
                onStatePaused();
                break;
            case PLAYBACK_COMPLETED:
                onStatePlaybackCompleted();
                break;

                default:
        }
    }

    @Override
    public void onStateError() {

    }

    @Override
    public void onStateIdle() {

    }

    @Override
    public void onStatePrepare() {

    }

    @Override
    public void onStatePrepareAsync() {

    }

    @Override
    public void onStatePrepared() {

    }

    @Override
    public void onStatePlaying() {

    }

    @Override
    public void onStatePaused() {

    }

    @Override
    public void onStatePlaybackCompleted() {

    }

    @Override
    public void onSeekComplete() {

    }

    @Override
    public void onBufferingUpdate(int progress) {

    }

    @Override
    public void onInfo(int what, int extra) {

    }

    @Override
    public void onProgressUpdate(int progress, long position, long duration) {

    }

    @Override
    public void onEnterSecondScreen() {

    }

    @Override
    public void onExitSecondScreen() {

    }
}
