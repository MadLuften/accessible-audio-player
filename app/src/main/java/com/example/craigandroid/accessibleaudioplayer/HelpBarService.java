// Copyright 2016 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.example.craigandroid.accessibleaudioplayer;

import android.accessibilityservice.AccessibilityService;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayDeque;
import java.util.Deque;

public class HelpBarService extends AccessibilityService {

    FrameLayout mLayout;

    @Override
    protected void onServiceConnected()
    {
        WindowManager winMan = (WindowManager) getSystemService(WINDOW_SERVICE);
        mLayout = new FrameLayout(this);
        WindowManager.LayoutParams layParams = new WindowManager.LayoutParams();
        layParams.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        layParams.format = PixelFormat.TRANSLUCENT;
        layParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layParams.gravity = Gravity.TOP;
        LayoutInflater layInfl = LayoutInflater.from(this);
        layInfl.inflate(R.layout.help_bar, mLayout);
        winMan.addView(mLayout, layParams);

        setVolUpBtn();
        setVolDownBtn();
        setScrollUpBtn();
        setScrollDownBtn();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {}

    @Override
    public void onInterrupt() {}

    private void setVolUpBtn()
    {
        Button volUpBtn = mLayout.findViewById(R.id.volume_up);
        volUpBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AudioManager serviceAudioMan = (AudioManager) getSystemService(AUDIO_SERVICE);
                serviceAudioMan.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            }
        });
    }

    private void setVolDownBtn()
    {
        Button volUpBtn = mLayout.findViewById(R.id.volume_down);
        volUpBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AudioManager serviceAudioMan = (AudioManager) getSystemService(AUDIO_SERVICE);
                serviceAudioMan.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
            }
        });
    }

    /* ***
        Accessibility services don't have access to views,
        they have a tree of nodes which mimic the on screen views
     *** */

    // method for searching tree to find scrollable nodes
    private AccessibilityNodeInfo findScrollNode(AccessibilityNodeInfo root)
    {
        Deque<AccessibilityNodeInfo> deque = new ArrayDeque<>();
        deque.add(root);
        while(!deque.isEmpty())
        {
            AccessibilityNodeInfo node = deque.removeFirst();
            if(node.getActionList().contains(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD))
            {
                return node;
            }
            for(int i=0; i<node.getChildCount(); i++)
            {
                deque.addLast(node.getChild(i));
            }
        }

        return null;
    }

    private void setScrollUpBtn()
    {
        Button scrollUpBtn = mLayout.findViewById(R.id.scroll_up);
        scrollUpBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // try find scrollable node on screen
                AccessibilityNodeInfo scrollable = findScrollNode(getRootInActiveWindow());
                if(scrollable != null)
                {
                    // perform scroll on scrollable view
                    scrollable.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD.getId());
                }
            }
        });
    }

    private void setScrollDownBtn()
    {
        Button scrollDownBtn = mLayout.findViewById(R.id.scroll_down);
        scrollDownBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // try find scrollable node on screen
                AccessibilityNodeInfo scrollable = findScrollNode(getRootInActiveWindow());
                if(scrollable != null)
                {
                    // perform scroll on scrollable view
                    scrollable.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD.getId());
                }
            }
        });
    }




}
