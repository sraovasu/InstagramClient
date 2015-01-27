package com.yahoo.instagramclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by sraovasu on 1/25/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    public InstagramPhotosAdapter(Context context, ArrayList<InstagramPhoto> photos) {
        super(context, R.layout.item_photo, photos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        InstagramPhoto photo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        // Lookup view for data population
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        Picasso.with(getContext())
                .load(photo.imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(ivPhoto);

        ImageView ivUserPhoto = (ImageView) convertView.findViewById(R.id.ivUserPhoto);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.LTGRAY)
                .borderWidthDp(1)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(getContext())
                .load(photo.profilePictureUrl)
                .fit()
                .transform(transformation)
                .into(ivUserPhoto);

        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        tvUserName.setText(photo.userName);

        ImageView ivTimeAgo = (ImageView) convertView.findViewById(R.id.ivTimeAgo);
        Picasso.with(getContext()).load(R.drawable.clock).into(ivTimeAgo);

        TextView tvTimeAgo = (TextView) convertView.findViewById(R.id.tvTimeAgo);
        tvTimeAgo.setText(photo.timeAgo());

        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        tvCaption.setText(photo.caption);

        ImageView ivHeart = (ImageView) convertView.findViewById(R.id.ivHeart);
        Picasso.with(getContext()).load(R.drawable.heart).into(ivHeart);

        TextView tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formattedLikesCount = formatter.format(photo.likesCount);
        tvLikesCount.setText(formattedLikesCount+" likes");

        ImageView ivComment = (ImageView) convertView.findViewById(R.id.ivComment);
        Picasso.with(getContext()).load(R.drawable.comments).into(ivComment);

        TextView tvCommentCount = (TextView) convertView.findViewById(R.id.tvCommentCount);
        String formattedCommentsCount = formatter.format(photo.commentsCount);
        tvCommentCount.setText(formattedCommentsCount+" comments");

        int lastCommentIndex = photo.comments.size()-1;
        Comment lastComment = photo.comments.get(lastCommentIndex);

        TextView tvFromUser = (TextView) convertView.findViewById(R.id.tvFromUser);
        tvFromUser.setText(lastComment.fromUserName);

        TextView tvComment = (TextView) convertView.findViewById(R.id.tvComment);
        tvComment.setText(lastComment.text);

        // Return the completed view to render on screen
        return convertView;
    }
}
