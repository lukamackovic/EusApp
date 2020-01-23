package com.luka.mackovic.eus.ui.view;

import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textview.MaterialTextView;
import com.luka.mackovic.eus.R;
import com.luka.mackovic.eus.domain.model.News;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.view_news_details)
public class NewsDetailsView extends ConstraintLayout {

    @ViewById
    MaterialTextView newsTitleDetails;

    @ViewById
    MaterialTextView newsCreatedDateDetails;

    @ViewById
    MaterialTextView newsContentDetails;

    public NewsDetailsView(Context context) {
        super(context);
    }

    public void setNewsItems(News news) {
        newsTitleDetails.setText(news.getTitle());
        newsCreatedDateDetails.setText(getContext().getString(R.string.news_created, news.getCreatedDate()));
        newsContentDetails.setText(news.getContent());
    }
}