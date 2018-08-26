package com.example.media.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.media.MediaPlayerManager;
import com.example.media.abs.AbsControlPanel;
import com.example.media.utils.LogUtil;

/**
 * 播放视频的容器，容纳两个重要的View：
 * 1. ControlPanel 视频控制面板
 * 2. ResizeText 视频展现View
 *
 * @author ldg
 * @date 2018/8/22
 */
public class VideoView extends FrameLayout {

    private final String TAG = getClass().getSimpleName();
    private static final int VIEW_VIDEO = 0;
    private static final int VIEW_PANEL = 1;

    private ResizeTextureView mVideo;
    private AbsControlPanel mPanel;

    private Object mDataSource;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VideoView(@NonNull Context context) {
        super(context);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        mVideo = new ResizeTextureView(getContext());
        mPanel = new NormalControlPanel(getContext());
        mPanel.setTarget(this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mVideo, VIEW_VIDEO, layoutParams);
        addView(mPanel, VIEW_PANEL, layoutParams);
    }

    public void setDataSource(Object dataSource) {
        mDataSource = dataSource;
        MediaPlayerManager.getInstance().init(this);
    }

    public Object getDataSource() {
        return mDataSource;
    }

    public AbsControlPanel getPanel() {
        return mPanel;
    }

    public ResizeTextureView getVideo() {
        return mVideo;
    }
}
