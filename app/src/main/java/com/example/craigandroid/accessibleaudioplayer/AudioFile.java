package com.example.craigandroid.accessibleaudioplayer;

public class AudioFile
{
    private long audioID;
    private String title;
    private String author;
    private int duration;
    private String data;

    public AudioFile(long ID, String audioTitle, String audioAuthor, int dur, String dataPath)
    {
        audioID = ID;
        title = audioTitle;
        author = audioAuthor;
        duration = dur;
        data = dataPath;
    }

    public long getID(){return audioID;}
    public String getTitle(){return title;}
    public String getArtist(){return author;}
    public int getDuration(){return duration;}
    public String getData(){return data;}
}
