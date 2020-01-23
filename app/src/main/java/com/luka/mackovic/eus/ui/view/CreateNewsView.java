package com.luka.mackovic.eus.ui.view;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.luka.mackovic.eus.R;
import com.luka.mackovic.eus.domain.model.News;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.Collection;
import java.util.Objects;

@EViewGroup(R.layout.view_create_news)
public class CreateNewsView extends ConstraintLayout {

    @ViewById
    TextInputEditText newsTitleEditText;

    @ViewById
    TextInputEditText newsContentEditText;

    @ViewById
    MaterialButton saveNews;

    private ItemClickedRequestInterface itemClickedRequestInterface;

    public void setItemClickedListener(ItemClickedRequestInterface itemClickedRequestInterface) {
        this.itemClickedRequestInterface = itemClickedRequestInterface;
    }

    @Click
    void saveNews() {
        saveNews.setVisibility(GONE);
        itemClickedRequestInterface.itemClick(ItemClickAction.SAVE_BUTTON_CLICKED);
    }

    private News news;

    @AfterViews
    void init() {
        if (news == null) {
            Toast.makeText(getContext(), getContext().getString(R.string.create_news_message), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.edit_news_message), Toast.LENGTH_SHORT).show();
            newsTitleEditText.setText(news.getTitle());
            newsContentEditText.setText(news.getContent());
        }
    }

    public CreateNewsView(Context context, News news) {
        super(context);
        this.news = news;
    }

    public CreateNewsView(Context context) {
        super(context);
    }

    public void setSaveButtonVisible() {
        saveNews.setVisibility(VISIBLE);
    }

    public News getNews() {
        News newNews;
        if (news != null) {
            newNews = news;
        } else {
            newNews = new News();
        }
        newNews.setTitle(Objects.requireNonNull(newsTitleEditText.getText()).toString());
        newNews.setContent(Objects.requireNonNull(newsContentEditText.getText()).toString());
        return newNews;
    }

    public void setErrors(Collection<CreateNewsFormValueType> validationViolationValueType) {
        for (CreateNewsFormValueType createNewsFormValueType : validationViolationValueType) {
            switch (createNewsFormValueType) {
                case TITLE:
                    newsTitleEditText.setError(getContext().getString(R.string.news_title_error_message));
                    break;
                case CONTENT:
                    newsContentEditText.setError(getContext().getString(R.string.news_content_error_message));
                    break;
            }
        }
    }

    public interface ItemClickedRequestInterface {
        void itemClick(ItemClickAction itemClickAction);
    }

    public enum ItemClickAction {
        SAVE_BUTTON_CLICKED
    }
}