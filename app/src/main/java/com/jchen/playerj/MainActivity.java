package com.jchen.playerj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jchen.playerj.PlayerJ.OnPrepareListener;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private int requestPermissionCode = 10086;

    private String[] requestPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private SurfaceView mSurfaceView;
    private SeekBar mSeekBar;
    private EditText mUrlEtv;
    private PlayerJ mPlayer;
    private static String URL = "rtmp://58.200.131.2:1935/livetv/hunantv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );
        setContentView(R.layout.activity_main);

        //布局
        mSurfaceView = findViewById(R.id.surface_view);
        Button play = findViewById(R.id.btn_play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playLocal();
                //playRtmp();
            }
        });
        mSeekBar = findViewById(R.id.seek_bar);
        mUrlEtv = findViewById(R.id.edt_url);
        mUrlEtv.setText(URL);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO: 2020/6/18
                mPlayer.seek(1000);
            }
        });


        //初始化播放器
        mPlayer = new PlayerJ();
        mPlayer.setSurfaceView(mSurfaceView);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if(PermissionChecker.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED){
                requestPermissions(requestPermission,requestPermissionCode);
            }
        }
    }

    private void playLocal() {
        File input = new File(getCacheDir(),"/input.mp4");
        Log.i("PlayerJ","input file: "+input.getAbsolutePath());
        //mPlayer.setDataSource("/sdcard/DCIM/Camera/v1080.mp4");
        mPlayer.setDataSource("/sdcard/DCIM/Camera/input.mp4");
        mPlayer.setOnPrepareListener(new PlayerJ.OnPrepareListener() {
            @Override
            public void onPrepare() {
                Log.d("PlayerJ", "******* onPrepare() *******");
                mPlayer.start();
            }
        });
        mPlayer.prepare();
    }

    private void playRtmp() {
        File input = new File(getCacheDir(),"/input.mp4");
        Log.i("PlayerJ","input file: "+input.getAbsolutePath());
        //mPlayer.setDataSource("/sdcard/DCIM/Camera/v1080.mp4");
        mPlayer.setDataSource(mUrlEtv.getEditableText().toString());
        mPlayer.setOnPrepareListener(new PlayerJ.OnPrepareListener() {
            @Override
            public void onPrepare() {
                Log.d("PlayerJ", "******* onPrepare() *******");
                mPlayer.start();
            }
        });
        mPlayer.prepare();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mPlayer) {
            mPlayer.close();
        }
    }
}
