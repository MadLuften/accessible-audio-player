package com.example.craigandroid.accessibleaudioplayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlaylistDatabaseHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME = "playlistsDB";
    private static final int DB_VERSION = 1;

    private static final String CREATE_TABLE_PLAYLIST_SONGS = "CREATE TABLE PLAYLIST_SONGS ("
                                                            + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                            + "FOREIGN KEY(playlistID) REFERENCES PLAYLISTS(playlistID), "
                                                            + "FOREIGN KEY(songID) REFERENCES SONGS(songID));";

    private static final String CREATE_TABLE_SONGS = "CREATE TABLE SONGS ("
                                                    + "songID INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                    + "songName TEXT, "
                                                    + "playlistID INTEGER, "
                                                    + "referenceID INTEGER);";

    private static final String CREATE_TABLE_PLAYLISTS = "CREATE TABLE PLAYLISTS ("
                                                        + "playlistID INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                        + "playlistName TEXT, "
                                                        + "count INTEGER);";


    PlaylistDatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION); // ctxt, name, cursor, version
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_PLAYLIST_SONGS);
        db.execSQL(CREATE_TABLE_SONGS);
        db.execSQL(CREATE_TABLE_PLAYLISTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
