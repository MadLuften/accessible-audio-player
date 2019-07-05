package com.example.craigandroid.accessibleaudioplayer;

import java.util.Collections;
import java.util.Comparator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class ListActivity extends AppCompatActivity implements AudioBrowser
{
    private ListView audioListView;
    private String uriForIntent;
    private Dialog popDialog;
    private Dialog detailDialog;
    private Dialog playlistDialog;
    private String fileTitle;
    private String fileArtist;
    private int fileDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_activity);
        audioListView = findViewById(R.id.audio_list);
        popDialog = new Dialog(this);
        detailDialog = new Dialog(this);
        playlistDialog = new Dialog(this);

        requestReadWritePerm();
    }

    // implemented from ActivityCompat.onRequestPermissionsResult interface
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch(requestCode) // Switch response from permission request
        {
            case PERMISSION_REQUEST_CODE: // Check response is pre-defined constant
                if(grantResults.length > 0) // Check grant results exist
                {
                    boolean readPermAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(readPermAccepted) // Check read perm is granted
                    {
                        audioToListView(); // Puts audio files in list view

                        Collections.sort(listOfAudio, new Comparator<AudioFile>() // Sort to alphabetical
                        {
                            public int compare(AudioFile audio1, AudioFile audio2)
                            {
                                return audio1.getTitle().compareTo(audio2.getTitle());
                            }
                        });

                        popupFromItemClicked(); // Show popup window
                    }
                    else
                    {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) // Check build version is 6.0 or higher
                        {
                            // Check if reason for permission should be shown
                            if(ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE))
                            {
                                // Show dialog
                                showMessageOKCancel("You need to allow permission to access files in storage",
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                // Request permission
                                                requestPermissions(new String[]{readPerm}, PERMISSION_REQUEST_CODE);
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }

            break;
        }
    }

    private void openPlayerWithSelection(String uri, String title)
    {
       Intent intent = new Intent(ListActivity.this, AudioPlayer.class);
       intent.putExtra("AudioUri", uri);
       intent.putExtra("AudioTitle", title);
       startActivity(intent);
    }

    private void popupFromItemClicked()
    {
       audioListView.setOnItemClickListener(new AdapterView.OnItemClickListener() // listen for click on item in list
       {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
           {
               Log.d("myTag", "item clicked");

               // get data about the clicked item
               AudioFile info = listOfAudio.get(pos); // get item
               uriForIntent = info.getData(); // get uri for audio player
               fileTitle = info.getTitle();
               fileArtist = info.getArtist();
               fileDuration = info.getDuration();
               showPopup(audioListView); // show popup
           }
       });
    }

    private void showPopup(View popupView)
    {
        Log.d("myTag", "showPopup called");
       popDialog.setContentView(R.layout.item_popup); // set view to popup dialog

       TextView popupTitleView = popDialog.findViewById(R.id.popup_title);
       Button popupPlayBtn = popDialog.findViewById(R.id.popup_play);
       Button popupPlistBtn = popDialog.findViewById(R.id.popup_playlist);
       Button popupDetailBtn = popDialog.findViewById(R.id.popup_details);

       popupTitleView.setText(fileTitle);

       popupPlayBtn.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {
               openPlayerWithSelection(uriForIntent, fileTitle);
           }
       });

       popupDetailBtn.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {
                showDetails();
           }
       });

       popupPlistBtn.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {

               Toast plistDlogToast = Toast.makeText(ListActivity.this, "!*Not yet implemented*!", Toast.LENGTH_LONG);
               plistDlogToast.show();

               //showPlaylistPopup();

               /*
               String userInput;
               EditText editText = new EditText(ListActivity.this);
               userInput = editText.getText().toString();
               Playlist plist1 = new Playlist(userInput);

               plists.addPlaylist(plist1);
               */
           }
       });

       popDialog.show();
    }

    /*
    private void showPlaylistPopup()
    {
       Log.d("myTag", "playlistPopup called");
       playlistDialog.setContentView(R.layout.playlist_popup);
       TextView plistTitleView = playlistDialog.findViewById(R.id.playlist_title);
       Button newPlistBtn = playlistDialog.findViewById(R.id.new_playlist);
       ListView plistListView = playlistDialog.findViewById(R.id.playlists);
       String plistTitle = "Playlist Title";

       //plistTitleView.setText(plistTitle);

       newPlistBtn.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {

           }
       });

       playlistDialog.show();
    }
    */

    private void showDetails()
    {
       detailDialog.setContentView(R.layout.popup_details);

       TextView detailsTitle = detailDialog.findViewById(R.id.details_title);
       TextView detailsArtist = detailDialog.findViewById(R.id.details_artist);

       detailsTitle.setText(fileTitle);
       detailsArtist.setText(fileArtist);

       detailDialog.show();
    }


    // AudioBrowser Interface methods

    public void getListOfAudio()
    {
        ContentResolver audioResolver = getContentResolver();
        Uri audioURI = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor audioCursor = audioResolver.query(audioURI, null,
                null, null, null);

        Log.d("myTag", "getListOfAudio called");

        if(audioCursor!=null && audioCursor.moveToFirst()) // Check if cursor query returns null
        {
            Log.d("myTag", "Audio retrieved");
            // Set columns for data
            int idColumn = audioCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int titleColumn = audioCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int authorColumn = audioCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
            int durationColumn = audioCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int dataColumn = audioCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do // Add information to listOfAudio as AudioFile
            {
                long thisID = audioCursor.getLong(idColumn);
                String thisTitle = audioCursor.getString(titleColumn);
                String thisAuthor = audioCursor.getString(authorColumn);
                int thisDuration = audioCursor.getInt(durationColumn);
                String thisData = audioCursor.getString(dataColumn);
                listOfAudio.add(new AudioFile(thisID, thisTitle, thisAuthor, thisDuration, thisData));
                Log.d("myTag", "Columns created");
            }while(audioCursor.moveToNext());

            audioCursor.close();
        }
    }

    public void audioToListView()
    {
        FileAdapter fileAdpt;
        getListOfAudio();
        fileAdpt = new FileAdapter(this, listOfAudio);
        audioListView.setAdapter(fileAdpt);

        Log.d("myTag", "Audio added to list view");
    }

    public void requestReadWritePerm()
    {
        ActivityCompat.requestPermissions(this, new String[]{readPerm}, PERMISSION_REQUEST_CODE);
    }

    public void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener)
    {
        new AlertDialog.Builder(ListActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
