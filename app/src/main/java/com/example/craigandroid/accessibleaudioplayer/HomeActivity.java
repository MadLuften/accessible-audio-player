package com.example.craigandroid.accessibleaudioplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button audioListBtn;
    Button playlistBtn;
    Button optionsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        audioListBtn = findViewById(R.id.button_0);
        playlistBtn = findViewById(R.id.button_1);
        optionsBtn = findViewById(R.id.button_2);

        audioListBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // view.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                startActivity(new Intent(HomeActivity.this, ListActivity.class));
            }
        });

        playlistBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(HomeActivity.this, PlaylistsActivity.class));
            }
        });

        optionsBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(HomeActivity.this, Options.class));
            }
        });
    }
}
