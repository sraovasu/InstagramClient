package com.yahoo.instagramclient;

import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sraovasu on 1/25/15.
 */
public class InstagramPhoto {
    public String userName;
    public String profilePictureUrl;
    public String imageUrl;
    public String caption;
    public int imageHeight;
    public int likesCount;
    public long createdTime;
    public int commentsCount;
    public ArrayList<Comment> comments;

    public String timeAgo(){
        return ((String) DateUtils.getRelativeTimeSpanString(this.createdTime,System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS)).toLowerCase();
    }
}
