package com.example.chencheng.testrecord;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Build;
import android.os.Environment;
import java.io.IOException;
import java.io.File;
import lwx.linin.aac.AAC;
import lwx.linin.aac.VoAAC;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

public class MainActivity extends AppCompatActivity
{
    private Button btnSpeak;
    private Button btnEnd;
    private Button btnPlay;

    private TextView content;
    private String fileName;

    private MediaRecorder recorder;
    private MediaPlayer player;

    private int sampleRateInHz = 16000;

    private AAC aac;
    private VoAAC voaac;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileName += "/fuckworld.aac";

        btnSpeak = (Button)findViewById(R.id.BtnSpeak);
        btnEnd = (Button)findViewById(R.id.BtnEnd);
        btnPlay = (Button)findViewById(R.id.BtnPlay);

        content = (TextView)findViewById(R.id.msg);

        btnSpeak.setOnClickListener(new StartRecord());
        btnEnd.setOnClickListener(new StopListener());
        btnPlay.setOnClickListener(new StartPlay());
    }

    class StartRecord implements OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            aac = new AAC(fileName);
            aac.sampleRateInHz(sampleRateInHz);
            aac.start();

            /*recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setAudioChannels(1);
            recorder.setOutputFile(fileName);

            try
            {
                recorder.prepare();
            }
            catch(IOException ex)
            {
                Log.e("chencheng", "prepare() failed!" + ex.getMessage());
            }
            content.setText("录音中..." + fileName);
            recorder.start();*/
        }
    }

    class StopListener implements OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if (aac != null)
            {
                aac.stop();
                aac = null;
            }
            /*if (recorder != null)
            {
                content.setText(R.string.ends);
                recorder.stop();
                recorder.release();
                recorder = null;
            }

            if (player != null)
            {
                content.setText(R.string.ends);
                player.stop();
                player.release();
                player = null;
            }*/
        }
    }


    class StartPlay implements OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            player = new MediaPlayer();
            try
            {
                File file = new File(fileName);
                boolean exists = file.exists();
                long size = file.length();
                if (exists)
                {
                    Log.e("XY", "文件大小：" + size);
                }
                player.setDataSource(fileName);
                player.prepare();
                player.start();
            }
            catch(IOException ex)
            {
                Log.e("XY", "player failed!" + ex.getMessage());
            }
        }
    }
}
