package com.jchen.playerj;

import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * setSurfaceView -> setDataSource -> prepare -> start
 */
public class PlayerJ implements SurfaceHolder.Callback {

    private static final String TAG = "PlayerJ";

    static {
        System.loadLibrary("playerj");
    }

    private native void native_prepare(String url);

    private native void native_set_surface(Surface surface);

    private native void native_start();

    private native void native_seek(long ms);//seek毫秒.

    private native void native_pause();

    private native void native_close();


    private SurfaceHolder mSurfaceHolder;
    private String mSource;

    private OnPrepareListener onPrepareListener;
    private OnProgressListener onProgressListener;
    private OnErrorListener onErrorListener;

    public void setOnPrepareListener(OnPrepareListener onPrepareListener) {
        this.onPrepareListener = onPrepareListener;
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("PlayerJ", "******* surfaceCreated()*******");
        setSurface(mSurfaceHolder.getSurface());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("PlayerJ", "******* surfaceChanged()*******");
        mSurfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("PlayerJ", "******* surfaceDestroyed()*******");
        native_pause();
    }

    public void setSurfaceView(SurfaceView surfaceView) {
        Log.d("PlayerJ", "******* setSurfaceView()*******");
        if (mSurfaceHolder != null) {
            mSurfaceHolder.removeCallback(this);
        }

        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }


    public void setSurface(Surface surface) {
        Log.d("PlayerJ", "******* setSurface()*******");
        native_set_surface(surface);
    }

    public void close() {
        Log.d("PlayerJ", "******* close()*******");
        native_close();
    }

    public void seek(long milliseconds) {
        Log.d("PlayerJ", "******* seek()*******");
        Log.d("PlayerJ", "milliseconds = " + milliseconds);
        native_seek(milliseconds);
    }

    public void start() {
        Log.d("PlayerJ", "******* start()*******");
        native_start();
    }

    public void setDataSource(String url) {
        Log.d("PlayerJ", "******* setDataSource()*******");
        Log.d("PlayerJ", "url = " + url);
        mSource = url;
    }

    public void prepare() {
        Log.d("PlayerJ", "******* prepare() *******");
        native_prepare(mSource);
    }


    /***回调方法***/
    /**
     * c层准备完毕.
     */
    void onPrepare() {

        if (null != onPrepareListener) onPrepareListener.onPrepare();
    }

    /**
     * 回调播放进度.
     *
     * @param progress
     */
    void onProgress(int progress) {
        if (null != onProgressListener) onProgressListener.onProgress(progress);
    }

    /**
     * 播放出错.
     *
     * @param errorCode
     */
    void onError(int errorCode) {

        if (null != onErrorListener) onErrorListener.onError(errorCode);
    }


    public interface OnPrepareListener {
        void onPrepare();
    }

    public interface OnProgressListener {
        void onProgress(int progress);
    }

    public interface OnErrorListener {
        void onError(int errorCode);
    }
}
