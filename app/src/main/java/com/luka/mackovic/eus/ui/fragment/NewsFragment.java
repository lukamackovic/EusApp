package com.luka.mackovic.eus.ui.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.luka.mackovic.eus.R;
import com.luka.mackovic.eus.domain.enumeration.SortDirection;
import com.luka.mackovic.eus.domain.model.News;
import com.luka.mackovic.eus.ui.activity.CreateNewsActivity_;
import com.luka.mackovic.eus.ui.activity.HomeActivity_;
import com.luka.mackovic.eus.ui.adapter.NewsAdapter;
import com.luka.mackovic.eus.usecase.crud.NewsCrudUseCase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.disposables.Disposable;

@EFragment(R.layout.fragment_news)
public class NewsFragment extends DaggerFragment
        implements Spinner.OnItemSelectedListener,
        NewsAdapter.NewsClickListener,
        Toolbar.OnClickListener {

    private static final String DATE_CREATED = "date created";
    private static final String TITLE = "title";

    private static final Comparator<News> sortAscendingDateCreated = (article, nextArticle) ->
            article.getCreatedDate().compareTo(nextArticle.getCreatedDate());
    private static final Comparator<News> sortDescendingDateCreated = (article, nextArticle) ->
            nextArticle.getCreatedDate().compareTo(article.getCreatedDate());

    private static final Comparator<News> sortAscendingTitle = (article, nextArticle) ->
            article.getTitle().compareTo(nextArticle.getTitle());
    private static final Comparator<News> sortDescendingTitle = (article, nextArticle) ->
            nextArticle.getTitle().compareTo(article.getTitle());

    @Inject
    NewsCrudUseCase newsCrudUseCase;

    @ViewById
    CoordinatorLayout newsFragmentContentView;

    @ViewById
    RecyclerView newsRecyclerView;

    @ViewById
    ImageView sortNews;

    @ViewById
    Spinner sortByNews;

    @ViewById
    Toolbar toolbar;

    @Bean
    NewsAdapter newsAdapter;

    @Pref
    SharedPreferences_ sharedPreferences;

    @Click
    void sortNews() {
        if (SortDirection.ASCENDING == SortDirection.valueOf(sharedPreferences.sortNewsDirection().get())) {
            sharedPreferences.sortNewsDirection().put(SortDirection.DESCENDING.name());
            sharedPreferences.imageNewsResource().put(R.drawable.ic_sort_descending);
        } else {
            sharedPreferences.sortNewsDirection().put(SortDirection.ASCENDING.name());
            sharedPreferences.imageNewsResource().put(R.drawable.ic_sort_ascending);
        }
        initView();
    }

    @Click
    void addNewNews() {
        CreateNewsActivity_.intent(this).start();
    }

    private Disposable disposable;

    @AfterViews
    void init() {
        newsRecyclerView.setHasFixedSize(true);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initView();
        initSpinner();
    }

    private void initView() {
        disposable = newsCrudUseCase.getNews()
                .subscribe(this::updateUI, this::showErrorMessage);
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.news_sort_by_list, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sortByNews.setAdapter(adapter);
        sortByNews.setSelection(adapter.getPosition(sharedPreferences.sortByNews().get()));
        sortByNews.setOnItemSelectedListener(this);
    }

    private void showErrorMessage(Throwable throwable) {
        Snackbar.make(newsFragmentContentView, Objects.requireNonNull(getContext())
                .getString(R.string.error_retrieving_news), Snackbar.LENGTH_SHORT).show();
    }

    private void updateUI(List<News> newsList) {
        if (newsList == null || newsList.isEmpty()) {
            Snackbar.make(newsFragmentContentView, Objects.requireNonNull(getContext())
                    .getString(R.string.no_news_error_message), Snackbar.LENGTH_LONG).show();
        } else {
            SortDirection desiredSortDirection = SortDirection.valueOf(sharedPreferences.sortNewsDirection().get());

            switch (sharedPreferences.sortByNews().get()) {
                case DATE_CREATED:
                    Collections.sort(newsList, desiredSortDirection == SortDirection.ASCENDING
                            ? sortAscendingDateCreated
                            : sortDescendingDateCreated
                    );
                    break;
                case TITLE:
                    Collections.sort(newsList, desiredSortDirection == SortDirection.ASCENDING
                            ? sortAscendingTitle
                            : sortDescendingTitle
                    );
                    break;
            }
            newsAdapter.setArticles(newsList, this);
            newsRecyclerView.setAdapter(newsAdapter);
        }
        sortNews.setImageResource(sharedPreferences.imageNewsResource().get());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sharedPreferences.sortByNews().put(parent.getItemAtPosition(position).toString());
        initView();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onItemLongClick(String newsId, String newsTitle) {
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(getString(R.string.news_toolbar_title, newsTitle));
        toolbar.setNavigationOnClickListener(this);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.delete:
                    newsCrudUseCase.deleteNews(newsId);
                    HomeActivity_.intent(getContext()).start();
                    return true;
                case R.id.edit:
                    CreateNewsActivity_.intent(getContext()).newsId(newsId).start();
                    return true;
            }
            return false;
        });
    }

    @Override
    public void onClick(View v) {
        toolbar.setVisibility(View.GONE);
    }
}