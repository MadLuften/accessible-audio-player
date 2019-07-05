package com.example.craigandroid.accessibleaudioplayer;

import android.Manifest;
import android.content.DialogInterface;

import java.util.ArrayList;

public interface AudioBrowser
{
    int PERMISSION_REQUEST_CODE = 200;
    String readPerm = Manifest.permission.READ_EXTERNAL_STORAGE;
    ArrayList<AudioFile> listOfAudio = new ArrayList<>();

    // onRequestPermissionsResult is an overridden method from an interface within ActivityCompat
    // therefore it is not declared here

    void getListOfAudio();
    void audioToListView();
    void requestReadWritePerm();
    void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener);
}
