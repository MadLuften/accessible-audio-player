package com.example.craigandroid.accessibleaudioplayer;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class PlaylistsActivity extends AppCompatActivity implements AudioBrowser
{
    private ArrayList<Playlist> playlists;
    private ArrayList<String> filesPicked;
    //private ArrayAdapter<Playlist> playlistAdpt;         *** These fields will be for the playlist implementation
    //private TextView playlistTitle;
    //private String fileID;
    //private Playlist playlist;
    //private SQLiteDatabase db;
    private ListView recycledList;
    private EditText playlistNameInput;
    private String playlistName;
    private int count = 0;
    private int clickCounter;
    private Dialog plistNameInputDialog;
    private Button addPlaylistBtn;
    private TextView playlistChosenName;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists_activity);

        plistNameInputDialog = new Dialog(this);
        recycledList = findViewById(R.id.recycled_list);
        addPlaylistBtn = findViewById(R.id.button_10);
        playlistChosenName = findViewById(R.id.playlist_name);
        playlists = new ArrayList<>();
        //playlistTitle = findViewById(R.id.playlist_title);
        //db = openOrCreateDatabase("playlistsDB", MODE_PRIVATE,null);

        Toast pListToast = Toast.makeText(this, "!*Prototype page, leads to dead ends*!", Toast.LENGTH_LONG);
        pListToast.show();

        requestReadWritePerm();
        playlistToListView();

        addPlaylistBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showAndGetNameInput();
                Log.d("myTag", " " + playlistName);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch(requestCode) // switch response from permission request
        {
            case PERMISSION_REQUEST_CODE: // check response is pre-defined constant
                if(grantResults.length > 0) // check grant results exist
                {
                    boolean readPermAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(readPermAccepted) // check read perm is granted
                    {

                        Collections.sort(listOfAudio, new Comparator<AudioFile>() // Sort to alphabetical
                        {
                            public int compare(AudioFile audio1, AudioFile audio2)
                            {
                                return audio1.getTitle().compareTo(audio2.getTitle());
                            }
                        });
                    }
                    else
                    {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) // check build version is 6.0 or higher
                        {
                            // check if reason for permission should be shown
                            if(shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE))
                            {
                                // show dialog
                                showMessageOKCancel("You need to allow permission to access files in storage",
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                // request permission
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

    //public int getCount(){return count;}

    private void playlistToListView()
    {
        // getListOfPlaylists();
        ArrayAdapter<Playlist> playlistAdpt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        recycledList.setAdapter(playlistAdpt);
        showAddBtnHideTitle();
    }



    public void addPlaylist(Playlist playlist)
    {
        playlists.add(playlist);
        count++;
    }

    private void removePlaylist(Playlist playlist)
    {
        if(playlist != null)
        {
            playlists.remove(playlist);
            count--;
        }
    }

    private void removeAllPlaylists()
    {
        for (Playlist plist:playlists)
        {
            playlists.remove(plist);
        }
    }

    private void sortPlaylistsByName()
    {

    }

    public void showAndGetNameInput()
    {
        Log.d("myTag", "showAndGetNameInput called");
        plistNameInputDialog.setContentView(R.layout.plistname_input_dialog);
        playlistNameInput = plistNameInputDialog.findViewById(R.id.playlist_input_name);
        Button acceptNameBtn = plistNameInputDialog.findViewById(R.id.accept_name_button);

        audioToListView();

        acceptNameBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                playlistName = playlistNameInput.getText().toString();
                plistNameInputDialog.dismiss();
                Log.d("myTag", playlistName);
                Toast tempToast = Toast.makeText(PlaylistsActivity.this, "!*This page is in development*!", Toast.LENGTH_LONG);
                tempToast.show();
            }
        });

        plistNameInputDialog.show();
    }

    private void hideAddBtnShowTitle()
    {
        addPlaylistBtn.setVisibility(View.INVISIBLE);
        playlistChosenName.setVisibility(View.VISIBLE);
    }

    private void showAddBtnHideTitle()
    {
        addPlaylistBtn.setVisibility(View.VISIBLE);
        playlistChosenName.setVisibility(View.INVISIBLE);
    }

    private void listenForMultichoice()
    {
        filesPicked = new ArrayList<>(); // array for tracking 'picked' filed

        recycledList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                AudioFile pickedFile = listOfAudio.get(position); // get position of selected item
                long longID = pickedFile.getID(); // get id of selected item
                String pickedID = Long.toString(longID); // convert id to string
                clickCounter++;

                if(clickCounter % 2 != 0) // check if odd
                {
                    // change colour to blue

                    filesPicked.add(pickedID); // add file to filesPicked
                }
                else
                {
                    // remove blue colour
                    if(filesPicked.contains(pickedID)) // if id is in filesPicked
                    {
                        filesPicked.remove(pickedID); // remove from filesPicked
                    }
                }

                /*for(int i = 0; i < filesPicked.size(); i++)
                {
                    //Log.d("picked-files", filesPicked.to);
                }*/

            }
        });
        // change colour
    }


    // AudioBrowser Interface methods

    public void getListOfAudio()
    {
        ContentResolver audioResolver = getContentResolver();
        Uri audioURI = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor audioCursor = audioResolver.query(audioURI, null,
                null, null, null);

        Log.d("myTag", "getListOfAudio called");

        if(audioCursor!=null && audioCursor.moveToFirst()) // check if cursor query returns null
        {
            Log.d("myTag", "Audio retrieved");
            // Set columns for data
            int titleColumn = audioCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = audioCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
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
        getListOfAudio();
        FileAdapter fileAdpt = new FileAdapter(this, listOfAudio);
        recycledList.setAdapter(fileAdpt); // Recycle view
        recycledList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        recycledList.setItemsCanFocus(false);
        hideAddBtnShowTitle();
        listenForMultichoice();


        Log.d("myTag", "Audio added to list view");
    }

    public void requestReadWritePerm()
    {
        ActivityCompat.requestPermissions(this, new String[]{readPerm}, PERMISSION_REQUEST_CODE);
    }

    public void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener)
    {
        new AlertDialog.Builder(PlaylistsActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
