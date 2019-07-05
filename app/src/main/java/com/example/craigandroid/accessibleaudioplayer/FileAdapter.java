package com.example.craigandroid.accessibleaudioplayer;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FileAdapter extends BaseAdapter
{
    private ArrayList<AudioFile> fileList;
    private LayoutInflater fileInf;

    FileAdapter(Context cntxt, ArrayList<AudioFile> list) // default access modifier: package-private
    {
        fileList = list;
        fileInf = LayoutInflater.from(cntxt);
    }

    @Override
    public int getCount()
    {
        return fileList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Takes file details layout and inflates it to listview
        LinearLayout fileLayout = (LinearLayout)fileInf.inflate(R.layout.file_details, parent, false);
        TextView titleView = fileLayout.findViewById(R.id.file_title);
        TextView authorView = fileLayout.findViewById(R.id.file_author);
        AudioFile currentFile = fileList.get(position); // Get current song
        titleView.setText(currentFile.getTitle()); // Set title to title view in file details
        authorView.setText(currentFile.getArtist()); // Set artist to artist view in file details
        fileLayout.setTag(position); // Store data of current song to layout

        return fileLayout;
    }
}
