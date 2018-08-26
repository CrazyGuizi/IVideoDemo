package com.example.media.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.media.MediaPlayer;
import com.example.media.MediaPlayerManager;
import com.example.media.PlayerState;
import com.example.media.R;
import com.example.media.abs.AbsControlPanel;
import com.example.media.utils.LogUtil;
import com.example.media.utils.Utils;

/**
 * @author ldg
 * @date 2018/8/23
 */
public class NormalControlPanel extends AbsControlPanel implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private final String TAG = getClass().getSimpleName();

    //    private ConstraintLayout mPanel;
    private ImageView mCover;
    // 网络改变时出现
    private LinearLayout mConnectionWarn;
    // 提示网络改变信息
    private TextView mConnectionState;
    // 网络改变时继续播放
    private Button mKeepPlaying;
    private ImageView mBack;
    private TextView mTitle;
    private ImageView mExpand;
    // 播放或暂停
    private ImageView mPlayState;
    // 当前进度
    private TextView mPosition;
    // 拖动滑块
    private SeekBar mSeekBar;
    // 总时长
    private TextView mDuration;

    // 面板自动消失时长
    private static final long DISMISS_SECONDS = 3000L;

    private View[] mPanel;

    // 用于设计面板自动消失
    private Runnable mDismissTask = new Runnable() {
        @Override
        public void run() {
            if (MediaPlayerManager.getInstance().getCurrentVideoView() == mTarget &&
                    MediaPlayerManager.getInstance().getPlayerState() == PlayerState.PLAYING)
                hideUI(mPanel);
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NormalControlPanel(@NonNull Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NormalControlPanel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NormalControlPanel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NormalControlPanel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int getResId() {
        return R.layout.normal_panel;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView(Context context) {
        super.initView(context);
        mCover = findViewById(R.id.iv_panel_cover);
        mConnectionWarn = findViewById(R.id.ll_connection_warn);
        mConnectionState = findViewById(R.id.tv_connection_state_msg);
        mKeepPlaying = findViewById(R.id.btn_keep_playing);
        mBack = findViewById(R.id.iv_panel_back);
        mTitle = findViewById(R.id.tv_video_title);
        mExpand = findViewById(R.id.iv_video_expand);
        mPlayState = findViewById(R.id.iv_video_play_state);
        mPosition = findViewById(R.id.tv_video_position);
        mSeekBar = findViewById(R.id.seek_bar_video_seek);
        mDuration = findViewById(R.id.video_duration);
        mTitle = findViewById(R.id.tv_video_title);

        mPanel = new View[]{mBack,
                mTitle,
                mExpand,
                mPlayState,
                mPosition,
                mSeekBar,
                mDuration,
                mTitle};

        initEvent();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initEvent() {
//        mSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.video_seek_bar_progress, null));
//        mSeekBar.setThumb(getResources().getDrawable(R.drawable.video_seek_bar_thumb, null));
        registerListener();
    }

    // 注册监听
    private void registerListener() {
        mCover.setOnClickListener(this);
        mKeepPlaying.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mExpand.setOnClickListener(this);
        mPlayState.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);

        mKeepPlaying.setOnClickListener(this);

        // 点击控制面板这个父容器时的事件
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(TAG, "touch panel");
                touchPanel();
            }
        });
    }

    // 开始计时
    private void startDismissTask() {
        cancelDismissTask();
        postDelayed(mDismissTask, DISMISS_SECONDS);
    }

    // 取消计时
    private void cancelDismissTask() {
        Handler handler = getHandler();
        if (mDismissTask != null && handler != null)
            handler.removeCallbacks(mDismissTask);
    }

    // 控件点击事件
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.iv_panel_cover) {
            LogUtil.d(TAG, "click Cover");
            touchCover();
        } else if (i == R.id.iv_panel_back) {
            MediaPlayerManager.getInstance().back();
        } else if (i == R.id.iv_video_expand) {
            changeWindowType();
        } else if (i == R.id.iv_video_play_state) {
            changeState();
        } else if (i == R.id.btn_keep_playing) {
            hideUI(mConnectionWarn);
            // 控制面板可点击
            setClickable(true);
            changeState();
        }

    }

    private void changeWindowType() {
        if (MediaPlayerManager.getInstance().getWindowType() == WindowType.NORMAL)
            MediaPlayerManager.getInstance().setWindowType(WindowType.ALL_SCREEN);
            // todo 改变窗口
        else {
            MediaPlayerManager.getInstance().setWindowType(WindowType.NORMAL);
            // todo 改变窗口
        }
    }

    // 播放暂停
    private void changeState() {
        if (MediaPlayerManager.getInstance().getPlayerState() ==
                PlayerState.PLAYING) {
            MediaPlayerManager.getInstance().pause();
        } else {
            MediaPlayerManager.getInstance().start();
        }
    }

    // 播放状态
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStatePlaying() {
        mPlayState.setBackground(getResources().getDrawable(R.drawable.video_pause, null));
        startDismissTask();
    }

    // 暂停状态
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStatePaused() {
        mPlayState.setBackground(getResources().getDrawable(R.drawable.video_play, null));
    }

    // 视频播放完成后显示控制面板
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStatePlaybackCompleted() {
        mPlayState.setBackground(getResources().getDrawable(R.drawable.video_play, null));
        if (mBack != null && mBack.getVisibility() == View.GONE)
            showUI(mPanel);
    }

    // 视频播放出错
    @Override
    public void onStateError() {
//        hideUI(mPanel);
//        showUI(mConnectionWarn);
//        hideUI(mKeepPlaying);
//        mConnectionState.setText(getResources().getString(R.string.error));
//        setClickable(false);
    }

    // 触碰控制面板
    private void touchPanel() {
        if (mTarget == null) return;
        if (mPlayState.getVisibility() == View.VISIBLE) {
            LogUtil.d(TAG, "mPanel" + mPanel);
            hideUI(mPanel);
            LogUtil.d(TAG, "hide panel");
        } else {
            showUI(mPanel);
            LogUtil.d(TAG, "show panel");
            startDismissTask();
        }
    }


    /**
     * 点击视频封面开始播放
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void touchCover() {
        if (mTarget == null) return;

        // 正在播放
        if (MediaPlayerManager.getInstance().getPlayerState() == PlayerState.PLAYING) {
            return;
        } else {
            hideUI(mCover);
            if (!checkInternet())
                return;
            LogUtil.d(TAG, "hide cover");
            startDismissTask();
            MediaPlayerManager.getInstance().start();
        }
    }

    // 检查网络是否能正常上网
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkInternet() {
        // 当前网络不可用
        if (!Utils.isNetConnected(getContext())) {
            showUI(mConnectionWarn);
            hideUI(mKeepPlaying);
            hideUI(mPanel);
            mConnectionState.setText(getResources().getString(R.string.no_net_warn));
            // 由于网络连接不上，控制面板不可点击
            setClickable(false);
            if (MediaPlayerManager.getInstance().isPlaying())
                onStatePaused();
            return false;
        } else {
            if (mConnectionWarn.getVisibility() == View.VISIBLE) {
                hideUI(mConnectionWarn);
            }
        }

        if (!Utils.isWifiConnected(getContext())) {
            showUI(mConnectionWarn);
            showUI(mKeepPlaying);
            hideUI(mPanel);
            mConnectionState.setText(getResources().getString(R.string.wifi_disconnection_warn));
            setClickable(false);
            if (MediaPlayerManager.getInstance().isPlaying())
                onStatePaused();
            return false;
        } else {
            if (mConnectionWarn.getVisibility() == View.VISIBLE) {
                hideUI(mConnectionWarn);
            }
        }

        setClickable(true);
        return true;
    }


    // 此方法在MediaPlayerManager的一个线程中被调用，所以更新需要转移到UI线程
    @Override
    public void onProgressUpdate(int progress, long position, long duration) {
        post(new Runnable() {
            @Override
            public void run() {
                mSeekBar.setProgress(progress);
                mDuration.setText(Utils.timeFormat(duration));
                mPosition.setText(Utils.timeFormat(position));
            }
        });

//        post(()-> {
//            mSeekBar.setProgress(progress);
//            mDuration.setText(Utils.timeFormat(duration));
//            mPosition.setText(Utils.timeFormat(position));
//        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        long total = MediaPlayerManager.getInstance().getDuration();
        long seek = (progress * total) / 100;
        MediaPlayerManager.getInstance().seekTo((int) seek);
        LogUtil.d(TAG, "seekbar stop and progress is " + progress);
    }

}
