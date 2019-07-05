package com.example.craigandroid.accessibleaudioplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class AudioPlayer extends AppCompatActivity {

    private MediaPlayer mPlayer;
    private AudioManager audioMan;
    private Uri audioUri;
    private Handler handler;
    private SeekBar timeControl;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        final Button playBtn = findViewById(R.id.play_button);
        Button stopBtn = findViewById(R.id.stop_button);
        Button restartBtn = findViewById(R.id.restart_button);
        timeControl = findViewById(R.id.progress_seek);
        TextView title = findViewById(R.id.player_title);
        handler = new Handler(); // handler is used for runnables on different threads
        playBtn.setContentDescription("Play");


        try
        {
            // get data passed from previous activity
            String filePath = getIntent().getExtras().getString( "AudioUri");
            String fileTitle = getIntent().getExtras().getString("AudioTitle");

            title.setText(fileTitle); // set title

            if(filePath != null) // if file path exists
            {
                audioUri = Uri.parse("file:///" + filePath); // parse file path to correct format
                Log.d("File Path: ", filePath);
            }
        }catch(NullPointerException npe)
        {
            Toast.makeText(this, "Could not find file path for song, ERROR: " + npe, Toast.LENGTH_LONG).show();
        }

        // create media player and audio manager
        mPlayer = MediaPlayer.create(this, audioUri);
        audioMan = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        int maxVolume = audioMan.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioMan.getStreamVolume(AudioManager.STREAM_MUSIC);

        // create volume control, set max and current values
        SeekBar volumeControl = findViewById(R.id.volume_seek);
        volumeControl.setMax(maxVolume); //
        volumeControl.setProgress(curVolume);

        // play/pause
        playBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!mPlayer.isPlaying())
                {
                    playAudio();
                    playBtn.setContentDescription("pause");
                }
                else
                {
                    pauseAudio();
                    playBtn.setContentDescription("play");
                }
            }
        });

        // stop
        stopBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stop();
            }
        });

        //restart
        restartBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                restart();
            }
        });

        // volume control listener
        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                Log.i("SeekBar Value ", Integer.toString(progress));
                // set volume when seekbar is changed
                audioMan.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar){}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar){}

        });

        timeControl.setMax(mPlayer.getDuration()); // set max value of seekbar to duration of audio

        // run time seekbar on ui thread
        /* *** This thread must be used
               to stop the seekbar 'jittering'
               the audio while it updates,
               this happens when it runs on
               the main thread
           *** */

        AudioPlayer.this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if(mPlayer != null)
                {
                    int sBarCurrentPosition = mPlayer.getCurrentPosition(); // get audio progress
                    timeControl.setProgress(sBarCurrentPosition); // update position of seekbar
                }
                handler.postDelayed(this, 1000);
            }
        });

        // time control listener
        timeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                Log.i("SeekBar Value ", Integer.toString(progress));
                if(mPlayer != null && fromUser)
                {
                    mPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar){}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar){}
        });
    }

    private void playAudio() {mPlayer.start();}

    private void pauseAudio() {mPlayer.pause();}

    private void stop()
    {
        // stop media and recreate to start from beginning
        mPlayer.stop();
        mPlayer = MediaPlayer.create(this, audioUri);
    }

    private void restart()
    {
        // stop media, recreate and play from beginning
        mPlayer.stop();
        mPlayer = MediaPlayer.create(this, audioUri);
        mPlayer.start();
    }



}

