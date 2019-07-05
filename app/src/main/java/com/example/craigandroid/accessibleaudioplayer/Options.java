package com.example.craigandroid.accessibleaudioplayer;

//import android.content.Intent;
//import android.os.Build;
//import android.provider.Settings;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
//import android.widget.Button;
import android.widget.Button;

public class Options extends AppCompatActivity
{
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Dialog prototypeDialog = new Dialog(this);
        prototypeDialog.setContentView(R.layout.prototype_dialog_back);
        Button goBackBtn = prototypeDialog.findViewById(R.id.go_back_btn);

        goBackBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        prototypeDialog.show();
    }

    // commented out for future development
    /*
    private void changeBrightness(View view, int brightness)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {

        }
    }*/


    // commented out for future development
    /*
    private void changeColour()
    {
        for(int i=0; i < 10; i++) // for all button elements
        {
            //
            int id = getResources().getIdentifier("button_"+i, "id", getPackageName());
            button[i] = (Button) findViewById(id);
        }
        //view.setBackgroundResource(color.);
    }*/
}
