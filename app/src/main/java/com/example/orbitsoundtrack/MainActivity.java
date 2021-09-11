package com.example.orbitsoundtrack;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // A list of button references
    //int[] buttons = { R.id.button01, R.id.button02, R.id.button03, R.id.button04, R.id.button05, R.id.button06, R.id.button07, R.id.button08 };
    // A list of audio files that match the buttons above
    int[] music = { R.raw.track01, R.raw.track02, R.raw.track03, R.raw.track04, R.raw.track05, R.raw.track06, R.raw.track07, R.raw.track08 };
    // A list of MediaPlayers so we can turn one off and the next on
    MediaPlayer[] mediaPlayer = { null, null, null, null, null, null, null, null};

    // If we're not playing anything, it's 0. If >0 we're playing something it's the index+1
    // If we're playing something and the index of what we're playing.
    int nowPlaying = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupButtons(); // https://developer.android.com/guide/topics/media/mediaplayer#java
    }

    public void setupMediaPlayer(int index) {
        Uri myUri;
        int resourceId;
        mediaPlayer[index] = new MediaPlayer();
        mediaPlayer[index].setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        mediaPlayer[index].setLooping(false);
        mediaPlayer[index].setOnCompletionListener(mp -> {
            // at this point we have to assume that nowPlaying has our index.
            // Stop previous
            int currentMedia = nowPlaying;
            nowPlaying = 0; // turns "it off"
            teardownMediaPlayer(currentMedia);
            currentMedia++;
            if (currentMedia == (music.length + 1)) {
                currentMedia = 1; // loop back around
            }
            // hit play
            playMedia(currentMedia);
        });
        resourceId = music[index];
        myUri = Uri.parse("android.resource://" + getPackageName() + "/" + resourceId);
        try {
            mediaPlayer[index].setDataSource(getApplicationContext(), myUri);
            mediaPlayer[index].prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void teardownMediaPlayer(int index) {
        mediaPlayer[index - 1].release();
        mediaPlayer[index - 1] = null;
    }

    public void setupButtons() {

        // In the future this will look better
        final Button button01 = findViewById(R.id.button01);
        final Button button02 = findViewById(R.id.button02);
        final Button button03 = findViewById(R.id.button03);
        final Button button04 = findViewById(R.id.button04);
        final Button button05 = findViewById(R.id.button05);
        final Button button06 = findViewById(R.id.button06);
        final Button button07 = findViewById(R.id.button07);
        final Button button08 = findViewById(R.id.button08);

        button01.setOnClickListener(v -> playMedia(1));
        button02.setOnClickListener(v -> playMedia(2));
        button03.setOnClickListener(v -> playMedia(3));
        button04.setOnClickListener(v -> playMedia(4));
        button05.setOnClickListener(v -> playMedia(5));
        button06.setOnClickListener(v -> playMedia(6));
        button07.setOnClickListener(v -> playMedia(7));
        button08.setOnClickListener(v -> playMedia(8));
    }

    public void playMedia(int resourceIndex) {
        if (nowPlaying > 0) {
            teardownMediaPlayer(nowPlaying);
        }
        setupMediaPlayer(resourceIndex - 1);
        nowPlaying = resourceIndex;
        mediaPlayer[resourceIndex-1].start();
    }
}