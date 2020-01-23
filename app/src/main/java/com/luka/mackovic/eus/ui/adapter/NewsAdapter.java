package com.luka.mackovic.eus.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.luka.mackovic.eus.domain.model.News;
import com.luka.mackovic.eus.ui.activity.NewsDetailsActivity_;
import com.luka.mackovic.eus.ui.view.NewsItemView;
import com.luka.mackovic.eus.ui.view.NewsItemView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

@EBean
public class NewsAdapter extends RecyclerViewAdapterBase<News, NewsItemView>{

    @RootContext
    Context context;

    private NewsClickListener newsClickListener;

    public void setArticles(List<News> news, NewsClickListener newsClickListener) {
        this.items = news;
        this.newsClickListener = newsClickListener;
    }

    @Override
    protected NewsItemView onCreateItemView(ViewGroup parent, int viewType) {
        return NewsItemView_.build(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<NewsItemView> holder, int position) {
        holder.getView().bind(items.get(position));
        holder.getView().setOnClickListener(v -> {
            String newsId = items.get(position).getId();
            NewsDetailsActivity_.intent(context).newsId(newsId).start();
        });
        holder.getView().setOnLongClickListener(v -> {
            newsClickListener.onItemLongClick(items.get(position).getId(),
                    items.get(position).getTitle());
            return true;
        });
    }

    public interface NewsClickListener {
        void onItemLongClick(String articleId, String articleTitle);
    }
}