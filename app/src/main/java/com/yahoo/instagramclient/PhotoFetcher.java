package com.yahoo.instagramclient;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sraovasu on 1/25/15.
 */

interface PhotoFetcherCallback {
    public void onSuccess(ArrayList<InstagramPhoto> photos);
    public void onFailure(Throwable throwable);
}

public class PhotoFetcher {
    private static String CLIENT_ID = "00d771fdab6941ce824097aa004701a1";
    private static  String POPULAR_PHOTOS_API = "https://api.instagram.com/v1/media/popular?client_id=";

    public static void fetchPopularPhotos(final PhotoFetcherCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(POPULAR_PHOTOS_API+CLIENT_ID, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                ArrayList<InstagramPhoto> photos = null;
                try {
                    photos = processResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onSuccess(photos);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    private static ArrayList<InstagramPhoto> processResponse(JSONObject response) throws JSONException {
        JSONArray dataArray = response.getJSONArray("data");
        ArrayList<InstagramPhoto> photos = new ArrayList<>();

        for (int i = 0; i < dataArray.length(); i++) {
            try{
                JSONObject photoJSON = dataArray.getJSONObject(i);
                InstagramPhoto photo = new InstagramPhoto();

                photo.userName = photoJSON.getJSONObject("user").getString("username");
                photo.profilePictureUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                photo.caption = photoJSON.getJSONObject("caption").getString("text");
                photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                JSONObject standardRes = photoJSON.getJSONObject("images").getJSONObject("standard_resolution");
                photo.imageUrl = standardRes.getString("url");
                photo.imageHeight = standardRes.getInt("height");
                String createdTime = photoJSON.getString("created_time");
                long seconds = Long.parseLong(createdTime);
                photo.createdTime = seconds*1000;

                photo.commentsCount = photoJSON.getJSONObject("comments").getInt("count");

                // process comments
                JSONArray commentsArray = photoJSON.getJSONObject("comments").getJSONArray("data");
                ArrayList<Comment> comments = new ArrayList<>();

                for (int j = 0; j < commentsArray.length(); j++) {
                    JSONObject commentJSON = commentsArray.getJSONObject(j);
                    Comment comment = new Comment();
                    comment.text = commentJSON.getString("text");
                    comment.fromUserName = commentJSON.getJSONObject("from").getString("username");
                    comment.profilePictureUrl = commentJSON.getJSONObject("from").getString("profile_picture");

                    comments.add(comment);
                }

                photo.comments = comments;

                photos.add(photo);
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
        return photos;
    }
}
