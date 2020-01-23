package com.luka.mackovic.eus.ui.view;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textview.MaterialTextView;
import com.luka.mackovic.eus.R;
import com.luka.mackovic.eus.domain.model.News;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.item_view_news)
public class NewsItemView extends RelativeLayout {

    @ViewById
    MaterialTextView newsTitle;

    @ViewById
    MaterialTextView newsContent;

    public NewsItemView(Context context) {
        super(context);
    }

    public void bind(News news) {
        this.newsTitle.setText(news.getTitle());
        this.newsContent.setText(news.getContent());
    }
}