package com.example.playmusic;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView noMusicTextView;
    ArrayList<AudioModel> songsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        noMusicTextView = findViewById(R.id.no_songs_text);

        //read file raw folder
        Field[] fields = R.raw.class.getFields();

        for (int i = 0; i < fields.length; i++) {
            String path = null;
            try {
                path = "android.resource://" + getPackageName() + "/" + fields[i].getInt(fields[i]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            System.out.println(path);
            // Read metadata
            Uri mediaPath = Uri.parse(path);
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(this, mediaPath);

            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

            AudioModel music = new AudioModel(path, title, duration);
            songsList.add(music);
        }

        for (int j = 0; j < songsList.size(); j++) {
            System.out.println("Title: " + songsList.get(j).getTitle());
            System.out.println("Duration: " + songsList.get(j).getDuration());
        }

        //test read
        System.out.println("Title: " + songsList.get(0).getTitle());
        if (songsList.size() == 0) {
            noMusicTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noMusicTextView.setVisibility(View.GONE);
            recyclerView.setAdapter(new MusicListAdapter(songsList,getApplicationContext()));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}