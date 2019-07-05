package com.example.craigandroid.accessibleaudioplayer;

import java.util.ArrayList;

public class Playlist
{
    private ArrayList<String> playlist;
    private String name;
    private int count;

    Playlist(String playlistName){name = playlistName;} // default access modifier: package-private

    public ArrayList<String> getPlaylist()
    {
        return playlist;
    }

    public String getPlaylistName(){return name;}

    public int getPlaylistCount(){return count;}

    public void add(String fileID)
    {
        playlist.add(fileID);
        count++;
    }

    public void remove(String fileID)
    {
        if(playlist != null)
        {
            playlist.remove(fileID);
            count--;
        }
    }

    public void changeName(String newName){name = newName;}

    public void sortFilesByName()
    {

    }
}
